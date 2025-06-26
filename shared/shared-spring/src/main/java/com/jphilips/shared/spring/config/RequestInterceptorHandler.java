package com.jphilips.shared.spring.config;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RequestInterceptorHandler {

    // Feign interceptor (common for all requests)
    @Bean
    public RequestInterceptor mdcRequestInterceptor() {
        return requestTemplate -> {
            String requestId = MDC.get("requestId");
            if (requestId != null) {
                requestTemplate.header("X-Request-Id", requestId);
            }

            String userId = MDC.get("userId");
            if (userId != null) {
                requestTemplate.header("X-User-Id", userId);
            }
        };
    }

}
