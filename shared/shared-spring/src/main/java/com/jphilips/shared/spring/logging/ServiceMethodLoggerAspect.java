package com.jphilips.shared.spring.logging;

import com.jphilips.shared.domain.exception.custom.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
public class ServiceMethodLoggerAspect {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void loggableEndpoints() {}

    @Around("loggableEndpoints()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            logStart(joinPoint);

            Object result = joinPoint.proceed();

            logEnd(joinPoint, start);
            return result;

        } catch (Throwable ex) {
            logException(joinPoint, ex);
            throw ex;
        }
    }

    private void logStart(JoinPoint joinPoint) {
        log.info("---> START: {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    private void logEnd(JoinPoint joinPoint, long startTimeMillis) {
        long duration = System.currentTimeMillis() - startTimeMillis;
        log.info("---> END: {}.{}(), Duration:{}ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                duration);
    }

    private void logException(JoinPoint joinPoint, Throwable ex) {
        String message;
        if (ex instanceof BaseException baseException) {
            message = baseException.getBaseErrorCode().getCode();
        } else {
            message = ex.getMessage();
        }

        log.error("---> EXCEPTION: {}.{}(): {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                message);
    }
}


