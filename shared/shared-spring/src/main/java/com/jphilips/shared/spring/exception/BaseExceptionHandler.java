package com.jphilips.shared.spring.exception;

import com.jphiilips.shared.domain.exception.errorcode.CommonErrorCode;
import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphiilips.shared.domain.exception.custom.InternalCallException;
import com.jphiilips.shared.domain.dto.ExceptionResponseDto;
import com.jphiilips.shared.domain.exception.util.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class BaseExceptionHandler {

    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiError(fieldError.getField(), fieldError.getDefaultMessage())
                )
                .toList();

        return exceptionResponseBuilder.buildFrom(new AppException(CommonErrorCode.VALIDATION_ERROR), errors, request);

    }

    @ExceptionHandler(InternalCallException.class)
    public ResponseEntity<ExceptionResponseDto> handleInternalCallExceptions(InternalCallException ex, WebRequest request) {

        log.warn("[InternalCallException] from {} - code: {}",
                ex.getSourceService(),
                ex.getExceptionResponseDto().code());

        HttpStatus status;

        try {
            status = HttpStatus.valueOf(ex.getExceptionResponseDto().status());
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(status).body(ex.getExceptionResponseDto());

    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponseDto> handleAppExceptions(AppException ex, WebRequest request) {

        log.warn("Handled business exception: {}", ex.getBaseErrorCode().getCode());

        return exceptionResponseBuilder.buildFrom(ex, request);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleOtherExceptions(Exception ex, WebRequest request) {

        log.error("Unhandled error: {}", ex.getMessage(), ex);

        return exceptionResponseBuilder.buildFrom(new AppException(CommonErrorCode.INTERNAL_SERVER_ERROR), request);

    }
}
