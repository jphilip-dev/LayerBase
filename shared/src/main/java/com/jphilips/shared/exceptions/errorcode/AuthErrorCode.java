package com.jphilips.shared.exceptions.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    EMAIL_EXISTS(HttpStatus.BAD_REQUEST),
    OWNERSHIP_MISMATCH(HttpStatus.FORBIDDEN),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST),
    USER_INACTIVE(HttpStatus.BAD_REQUEST),
    JWT_MISSING(HttpStatus.FORBIDDEN),
    JWT_EXPIRED(HttpStatus.FORBIDDEN),
    JWT_INVALID(HttpStatus.FORBIDDEN);

    private final HttpStatus status;
    private final String code;

    private static final String PREFIX = "AUTH_ERROR_";

    AuthErrorCode(HttpStatus status) {
        this.status = status;
        this.code = PREFIX + this.name();
    }

}
