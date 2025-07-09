package com.jphilips.shared.domain.exception.errorcode;

import lombok.Getter;

@Getter
public enum UserDetailsErrorCode implements BaseErrorCode {

    OWNERSHIP_MISMATCH(400),
    NOT_FOUND(400);

    private final int statusCode;
    private final String code;

    private static final String PREFIX = "USER_DETAILS_ERROR_";

    UserDetailsErrorCode(int statusCode) {
        this.statusCode = statusCode;
        this.code = PREFIX + this.name();
    }
}
