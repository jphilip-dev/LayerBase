package com.jphilips.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import com.jphilips.shared.exceptions.util.Error;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ExceptionResponseDto(
        LocalDateTime timestamp,
        int status,
        String error,
        String code,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String value,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<Error> errors,
        String path
) {
}
