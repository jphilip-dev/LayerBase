package com.jphilips.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.AuthErrorCode;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;
import com.jphilips.apigateway.config.RoleBasedAccessConfig;
import com.jphilips.shared.domain.dto.ExceptionResponseDto;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.shared.spring.exception.ExceptionResponseBuilder;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import com.jphilips.shared.spring.redis.util.CacheKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleBasedAccessConfig> {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final ExceptionResponseBuilder exceptionResponseBuilder;

    private final RedisHelper redisHelper;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${AUTH_SERVICE_URI}") String authServiceUrl,
                                             ObjectMapper objectMapper,
                                             ExceptionResponseBuilder exceptionResponseBuilder,
                                             RedisHelper redisHelper) {
        super(RoleBasedAccessConfig.class);
        this.objectMapper = objectMapper;
        this.exceptionResponseBuilder = exceptionResponseBuilder;
        this.redisHelper = redisHelper;
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(RoleBasedAccessConfig config) {
        return (exchange, chain) -> {

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token == null || token.isBlank()) {
                return writeErrorResponse(exchange, AuthErrorCode.UNAUTHORIZED);
            }

            // Check cache first
            UserResponseDto cached = redisHelper.get(CacheKeys.Auth.AUTH_TOKEN + token, UserResponseDto.class);
            Mono<UserResponseDto> userMono;


            if (cached != null) {
                log.info("Getting auth info from cache");
                userMono = Mono.just(cached);
            } else {
                // Fallback to WebClient if not in cache
                userMono = webClient.get()
                        .uri("/auth/validate")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .retrieve()
                        .bodyToMono(UserResponseDto.class)
                        .doOnNext(user -> {
                            // Store in Redis with short TTL (e.g. 3 mins)
                            redisHelper.put(CacheKeys.Auth.AUTH_TOKEN + token, user, Duration.ofMinutes(3),CacheKeys.Auth.AUTH_TOKEN_TAG + user.getId());
                        });
            }


            return userMono.flatMap(userDetails -> {

                        List<String> userRoles = userDetails.getRoles();

                        // Role Check
                        boolean authorized = userRoles.stream()
                                .anyMatch(role -> config.getRoles().stream()
                                        .map(String::trim)
                                        .map(String::toUpperCase)
                                        .anyMatch(r -> r.equals(role.toUpperCase())));

                        if (!authorized) {

                            // Log Unauthorized users trying to access Secured endpoints
                            log.warn("[req:{}] [user:{}] Forbidden access to {}. Required: {}, User Roles: {}",
                                    userDetails.getRequestId(),
                                    userDetails.getId(),
                                    exchange.getRequest().getURI().getPath(),
                                    config.getRoles(),
                                    userDetails.getRoles());

                            return writeErrorResponse(exchange, AuthErrorCode.FORBIDDEN);
                        }

                        // Log authorized access
                        log.info("[req:{}] [user:{}] -> {} {}",
                                userDetails.getRequestId(),
                                userDetails.getId(),
                                exchange.getRequest().getMethod(),
                                exchange.getRequest().getURI().getPath());


                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .headers(httpHeaders -> {
                                    httpHeaders.remove(HttpHeaders.AUTHORIZATION);
                                    httpHeaders.remove("X-User-Id");
                                    httpHeaders.remove("X-User-Email");
                                    httpHeaders.remove("X-Request-Id");
                                })
                                .header("X-User-Id", userDetails.getId().toString())
                                .header("X-User-Email", userDetails.getEmail())
                                .header("X-Request-Id", userDetails.getRequestId())
                                .build();

                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(mutatedExchange);
                    })
                    .onErrorResume(error -> {

                        log.error("[Gateway] Error calling auth service: {}", error.getMessage(), error);

                        if (error instanceof WebClientResponseException ex) {
                            exchange.getResponse().setStatusCode(ex.getStatusCode());

                            // Set content-type and return the structured body as-is
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            DataBuffer buffer = exchange.getResponse()
                                    .bufferFactory()
                                    .wrap(ex.getResponseBodyAsByteArray());

                            return exchange.getResponse().writeWith(Mono.just(buffer));
                        }

                        // FallBack Error
                        return writeErrorResponse(exchange, AuthErrorCode.UNAUTHORIZED);

                    });
        };
    }


    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, BaseErrorCode baseErrorCode) {

        ExceptionResponseDto dto = buildErrorResponse(baseErrorCode, exchange.getRequest().getURI().getPath());

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(dto);
            exchange.getResponse().setRawStatusCode(dto.status());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }

    private ExceptionResponseDto buildErrorResponse(BaseErrorCode baseErrorCode, String path) {
        return exceptionResponseBuilder.buildFrom(
                new AppException(baseErrorCode),
                path
        ).getBody();
    }


}

