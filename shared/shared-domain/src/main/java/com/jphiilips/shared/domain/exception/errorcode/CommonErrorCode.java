package com.jphiilips.shared.domain.exception.errorcode;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements BaseErrorCode {

    INTERNAL_CALL(500),
    INTERNAL_SERVER_ERROR(500),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    VALIDATION_ERROR(400),
    CONFLICT(409);

    private final int statusCode;
    private final String code;

    private static final String PREFIX = "ERROR_";

    CommonErrorCode(int statusCode) {
        this.statusCode = statusCode;
        this.code = PREFIX + this.name();
    }
}

