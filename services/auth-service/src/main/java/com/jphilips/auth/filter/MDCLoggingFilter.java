package com.jphilips.auth.filter;

import com.jphilips.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MDCLoggingFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Unique ID for the request
            MDC.put("requestId", UUID.randomUUID().toString());

            // try to extract userId, Login and Register endpoints won't have this
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    Long userId = jwtUtil.extractUserId(token); // Your custom method
                    if (userId != null) {
                        MDC.put("userId", userId.toString());
                    }
                } catch (Exception e) {
                    // Log or ignore invalid tokens; don't break request processing
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear(); // Always clear to prevent thread leaks
        }
    }
}
