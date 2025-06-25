package com.jphilips.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jphilips.shared.config.FeignCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class FeignCallerHelper extends FeignCaller {

    private final ObjectMapper objectMapper;

    public <T> T execute(String sourceService, Supplier<T> supplier) {
        return callWithErrorHandling(supplier, sourceService, objectMapper);
    }
}
