package com.jphiilips.shared.domain.exception.custom;

import com.jphiilips.shared.domain.exception.errorcode.BaseErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final BaseErrorCode baseErrorCode;

    public BaseException(BaseErrorCode baseErrorCode) {
        super();
        this.baseErrorCode = baseErrorCode;
    }

    public BaseException(BaseErrorCode baseErrorCode, String message) {
        super(message);
        this.baseErrorCode = baseErrorCode;
    }
}
