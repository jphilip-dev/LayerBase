package com.jphilips.shared.spring.exception;

import com.jphilips.shared.domain.dto.ExceptionResponseDto;
import com.jphilips.shared.domain.exception.custom.BaseException;
import com.jphilips.shared.domain.exception.util.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExceptionResponseBuilder {

    private final ErrorCodeAdapter errorCodeAdapter;

    public ResponseEntity<ExceptionResponseDto> buildFrom(BaseException ex, List<ApiError> errors, String path) {
        var httpStatus = errorCodeAdapter.covertRawStatus(ex.getBaseErrorCode());
        var code = ex.getBaseErrorCode().getCode();

        var exceptionResponseDto = ExceptionResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .code(code)
                .value(ex.getMessage() == null || ex.getMessage().isBlank() ? null : ex.getMessage())
                .errors(errors == null || errors.isEmpty() ? null : errors)
                .path(path)
                .build();

        return ResponseEntity.status(httpStatus).body(exceptionResponseDto);
    }

    public ResponseEntity<ExceptionResponseDto> buildFrom(BaseException ex, List<ApiError> errors, WebRequest request) {
        return buildFrom(
                ex,
                errors,
                request.getDescription(false).replace("uri=", "")
        );
    }

    public ResponseEntity<ExceptionResponseDto> buildFrom(BaseException ex, WebRequest request) {
        return buildFrom(ex, null, request);
    }

    public ResponseEntity<ExceptionResponseDto> buildFrom(BaseException ex, String path) {
        return buildFrom(ex, null, path);
    }


}
