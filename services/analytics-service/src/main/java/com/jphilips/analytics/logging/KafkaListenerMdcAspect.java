package com.jphilips.analytics.logging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jphilips.shared.domain.dto.kafka.AppEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListenerMdcAspect {

    private final ObjectMapper objectMapper;

    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object wrapKafkaListener(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String requestId = "unknown";
        String userId = "unknown";

        for (Object arg : args) {
            if (arg instanceof AppEvent<?> event) {
                requestId = event.getId() != null ? event.getId().toString() : "unknown";
                userId = event.getUserId() != null ? event.getUserId().toString() : "unknown";
                break;
            } else if (arg instanceof byte[] raw) {
                try {
                    AppEvent<?> parsed = objectMapper.readValue(raw, new TypeReference<>() {});
                    requestId = parsed.getId() != null ? parsed.getId().toString() : "unknown";
                    userId = parsed.getUserId() != null ? parsed.getUserId().toString() : "unknown";
                    break;
                } catch (Exception e) {
                    log.warn("Failed to parse byte[] as AppEvent for MDC");
                }
            }
        }

        try {
            MDC.put("requestId", requestId);
            MDC.put("userId", userId);
            return joinPoint.proceed();
        } finally {
            MDC.clear();
        }
    }
}

