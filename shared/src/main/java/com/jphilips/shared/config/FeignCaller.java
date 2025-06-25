package com.jphilips.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jphilips.shared.dto.ExceptionResponseDto;
import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.custom.InternalCallException;
import com.jphilips.shared.exceptions.errorcode.CommonErrorCode;
import feign.FeignException;

import java.util.function.Supplier;

public abstract class FeignCaller {

    protected <T> T callWithErrorHandling(
            Supplier<T> call,
            String sourceService,
            ObjectMapper objectMapper
    ) {
        try {
            return call.get();
        } catch (FeignException ex) {
            try {
                var dto = objectMapper.readValue(ex.contentUTF8(), ExceptionResponseDto.class);
                throw new InternalCallException(dto, sourceService);
            } catch (Exception e) {
                throw new AppException(CommonErrorCode.INTERNAL_CALL);
            }
        }
    }
}

