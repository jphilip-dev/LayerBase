package com.jphilips.shared.spring.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jphilips.shared.domain.exception.custom.ObjectMappingException;
import com.jphilips.shared.domain.exception.errorcode.AnalyticsErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectMapperHandler {

    private final ObjectMapper objectMapper;

    public <T> T convert(Object input, Class<T> targetType) {
        try {
            return objectMapper.convertValue(input, targetType);
        } catch (IllegalArgumentException e) {
            throw new ObjectMappingException(AnalyticsErrorCode.OBJECT_MISMATCH);
        }
    }
}
