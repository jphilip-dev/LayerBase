package com.jphilips.shared.exceptions.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements BaseErrorCode {

    INTERNAL_CALL(HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    CONFLICT(HttpStatus.CONFLICT);

    private final HttpStatus status;
    private final String code;

    private static final String PREFIX = "ERROR_";

    CommonErrorCode(HttpStatus status) {
        this.status = status;
        this.code = PREFIX + this.name();
    }
}
