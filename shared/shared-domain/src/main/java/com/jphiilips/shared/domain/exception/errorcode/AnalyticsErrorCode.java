package com.jphiilips.shared.domain.exception.errorcode;

import lombok.Getter;

@Getter
public enum AnalyticsErrorCode implements BaseErrorCode {

    NOT_FOUND(404);

    private final int statusCode;
    private final String code;

    private static final String PREFIX = "ANALYTICS_ERROR_";

    AnalyticsErrorCode(int statusCode) {
        this.statusCode = statusCode;
        this.code = PREFIX + this.name();
    }

}
