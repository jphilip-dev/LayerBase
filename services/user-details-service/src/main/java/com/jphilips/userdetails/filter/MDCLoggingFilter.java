package com.jphilips.userdetails.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MDCLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {

            String requestId = request.getHeader("X-Request-Id"); // or extract from token
            if (requestId != null) {
                MDC.put("requestId", requestId);
            }

            String userId = request.getHeader("X-User-Id"); // or extract from token
            if (userId != null) {
                MDC.put("userId", userId);
            }

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear(); // Always clear to prevent thread leaks
        }
    }
}
