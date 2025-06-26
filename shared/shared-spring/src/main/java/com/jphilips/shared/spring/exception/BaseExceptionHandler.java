package com.jphilips.shared.spring.exception;

import com.jphiilips.shared.domain.exception.errorcode.CommonErrorCode;
import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphiilips.shared.domain.exception.custom.BaseException;
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

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class BaseExceptionHandler {

    private final ErrorCodeAdapter errorCodeAdapter;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiError(fieldError.getField(), fieldError.getDefaultMessage())
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

        log.warn("Handled business exception: {}", ex.getBaseErrorCode().getCode());

        return buildResponseFrom(ex, request);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleOtherExceptions(Exception ex, WebRequest request) {

        log.error("Unhandled error: {}", ex.getMessage(), ex);

        return buildResponseFrom(new AppException(CommonErrorCode.INTERNAL_SERVER_ERROR), request);

    }

    // ----Helper-Methods----
    public ResponseEntity<ExceptionResponseDto> buildResponseFrom(BaseException ex, List<ApiError> errors, WebRequest request) {

        // Convert int to http status
        var httpStatus = errorCodeAdapter.covertRawStatus(ex.getBaseErrorCode());
        var code = ex.getBaseErrorCode().getCode();


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
