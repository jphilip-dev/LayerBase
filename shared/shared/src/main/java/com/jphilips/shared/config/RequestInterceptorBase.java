package com.jphilips.shared.config;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;

public class RequestInterceptorBase {

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
