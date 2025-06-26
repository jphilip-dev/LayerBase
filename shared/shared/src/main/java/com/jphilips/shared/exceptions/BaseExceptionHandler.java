package com.jphilips.shared.exceptions;

import com.jphilips.shared.dto.ExceptionResponseDto;
import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.custom.BaseException;
import com.jphilips.shared.exceptions.custom.InternalCallException;
import com.jphilips.shared.exceptions.errorcode.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.jphilips.shared.exceptions.util.Error;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public abstract class BaseExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        List<Error> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new Error(fieldError.getField(), fieldError.getDefaultMessage())
                )
                .toList();

        return buildResponseFrom(new AppException(CommonErrorCode.VALIDATION_ERROR), errors, request);

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

        log.warn("Handled business exception: {}", ex.getErrorCode().getCode());

        return buildResponseFrom(ex, request);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleOtherExceptions(Exception ex, WebRequest request) {

        log.error("Unhandled error: {}", ex.getMessage(), ex);

        return buildResponseFrom(new AppException(CommonErrorCode.INTERNAL_SERVER_ERROR), request);

    }

    // ----Helper-Methods----
    public ResponseEntity<ExceptionResponseDto> buildResponseFrom(BaseException ex, List<Error> errors, WebRequest request) {

        var httpStatus = ex.getErrorCode().getStatus();
        var code = ex.getErrorCode().getCode();


        var exceptionResponseDto = ExceptionResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .code(code)
                .value(ex.getMessage() == null || ex.getMessage().isBlank() ? null : ex.getMessage())
                .errors(errors == null || errors.isEmpty() ? null : errors)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(httpStatus).body(exceptionResponseDto);
    }

    public ResponseEntity<ExceptionResponseDto> buildResponseFrom(BaseException ex, WebRequest request) {
        return buildResponseFrom(ex, null, request);
    }
}
