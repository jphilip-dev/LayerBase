package com.jphilips.shared.exceptions.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserDetailsErrorCode implements BaseErrorCode{

    OWNERSHIP_MISMATCH(HttpStatus.BAD_REQUEST),
    NOT_FOUND(HttpStatus.BAD_REQUEST);

    private final HttpStatus status;
    private final String code;

    private static final String PREFIX = "USER_DETAILS_ERROR_";

    UserDetailsErrorCode(HttpStatus status) {
        this.status = status;
        this.code = PREFIX + this.name();
    }
}
