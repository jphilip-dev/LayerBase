package com.jphilips.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jphilips.apigateway.config.RoleBasedAccessConfig;
import com.jphilips.shared.dto.ExceptionResponseDto;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.shared.exceptions.errorcode.AuthErrorCode;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;
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

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleBasedAccessConfig> {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;


    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${AUTH_SERVICE_URI}") String authServiceUrl,
                                             ObjectMapper objectMapper) {
        super(RoleBasedAccessConfig.class);
        this.objectMapper = objectMapper;
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(RoleBasedAccessConfig config) {
        return (exchange, chain) -> {

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token == null || token.isBlank()) {
                return writeErrorResponse(exchange, AuthErrorCode.UNAUTHORIZED);
            }

            return webClient.get()
                    .uri("/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(UserResponseDto.class)
                    .flatMap(userDetails -> {

                        List<String> userRoles = userDetails.roles();

                        // Role Check
                        boolean authorized = userRoles.stream()
                                .anyMatch(role -> config.getRoles().stream()
                                        .map(String::trim)
                                        .map(String::toUpperCase)
                                        .anyMatch(r -> r.equals(role.toUpperCase())));

                        if (!authorized) {

                            // Log Unauthorized users trying to access Secured endpoints
                            log.warn("[req:{}] [user:{}] Forbidden access to {}. Required: {}, User Roles: {}",
                                    userDetails.requestId(),
                                    userDetails.id(),
                                    exchange.getRequest().getURI().getPath(),
                                    config.getRoles(),
                                    userDetails.roles());

                            return writeErrorResponse(exchange, AuthErrorCode.FORBIDDEN);
                        }

                        // Log authorized access
                        log.info("[req:{}] [user:{}] -> {} {}",
                                userDetails.requestId(),
                                userDetails.id(),
                                exchange.getRequest().getMethod(),
                                exchange.getRequest().getURI().getPath());



                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .headers(httpHeaders -> {
                                    httpHeaders.remove(HttpHeaders.AUTHORIZATION);
                                    httpHeaders.remove("X-User-Id");
                                    httpHeaders.remove("X-User-Email");
                                    httpHeaders.remove("X-Request-Id");
                                })
                                .header("X-User-Id", userDetails.id().toString())
                                .header("X-User-Email", userDetails.email())
                                .header("X-Request-Id", userDetails.requestId())
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


    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, BaseErrorCode errorCode) {

        ExceptionResponseDto dto = createErrorResponse(errorCode, exchange.getRequest().getURI().getPath());

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(dto);
            exchange.getResponse().setStatusCode(errorCode.getStatus());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }

    private ExceptionResponseDto createErrorResponse(BaseErrorCode errorCode, String path) {
        return ExceptionResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus().value())
                .error(errorCode.getStatus().getReasonPhrase())
                .code(errorCode.getCode())
                .path(path)
                .build();
    }



}
