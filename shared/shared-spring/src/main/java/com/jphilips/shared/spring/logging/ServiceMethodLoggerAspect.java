package com.jphilips.shared.spring.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "mdc.logging.service.audit", havingValue = "true")
public class ServiceMethodLoggerAspect {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void loggableEndpoints() {}

    @Around("loggableEndpoints()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {


        int level = Optional.ofNullable(MDC.get("indentLevel"))
                .map(Integer::parseInt)
                .orElse(0);
        MDC.put("indentLevel", String.valueOf(level + 1));
        MDC.put("indent", "    ".repeat(level));

        long start = System.currentTimeMillis();
        try {
            log.info(">START: {}.{}()",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());

            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;
            log.info(">END: {}.{}(), Duration:{}ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    duration);

            return result;

        } finally {
            // Decrease level or remove if root
            if (level == 0) {
                MDC.remove("indentLevel");
                MDC.remove("indent");
            } else {
                MDC.put("indentLevel", String.valueOf(level));
                MDC.put("indent", "    ".repeat(level - 1));
            }
        }
    }

}
