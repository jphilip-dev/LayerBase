package com.jphilips.shared.exceptions.custom;

import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public BaseException(BaseErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BaseException(BaseErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
