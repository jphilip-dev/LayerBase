package com.jphilips.auth.exceptions;

import com.jphiilips.shared.domain.dto.ExceptionResponseDto;
import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphiilips.shared.domain.exception.errorcode.AuthErrorCode;
import com.jphilips.shared.spring.exception.ExceptionResponseBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class AuthExceptionHandler {

    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionResponseDto> handleJwtExceptions(JwtException ex, WebRequest request){
        return exceptionResponseBuilder.buildFrom(
                new AppException(ex instanceof ExpiredJwtException ? AuthErrorCode.JWT_EXPIRED : AuthErrorCode.JWT_INVALID),
                request
        );
    }
}
