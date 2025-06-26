package com.jphiilips.shared.domain.exception.custom;

import com.jphiilips.shared.domain.dto.ExceptionResponseDto;
import lombok.Getter;

@Getter
public class InternalCallException extends RuntimeException{

    private final ExceptionResponseDto exceptionResponseDto;
    private final String sourceService; // e.g., "auth-service"


    public InternalCallException(ExceptionResponseDto exceptionResponseDto, String sourceService) {
        super();
        this.exceptionResponseDto = exceptionResponseDto;
        this.sourceService = sourceService;
    }

}
