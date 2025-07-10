package com.jphilips.shared.domain.exception.errorcode;

import lombok.Getter;

@Getter
public enum AuthErrorCode implements BaseErrorCode {

    FORBIDDEN(403),
    UNAUTHORIZED(401),

    USER_NOT_FOUND(404),
    EMAIL_EXISTS(400),
    OWNERSHIP_MISMATCH(403),
    PASSWORD_MISMATCH(400),
    USER_INACTIVE(400),
    JWT_MISSING(403),
    JWT_EXPIRED(403),
    JWT_INVALID(403);

    private final int statusCode;
    private final String code;

    private static final String PREFIX = "AUTH_ERROR_";

    AuthErrorCode(int statusCode) {
        this.statusCode = statusCode;
        this.code = PREFIX + this.name();
    }

}
