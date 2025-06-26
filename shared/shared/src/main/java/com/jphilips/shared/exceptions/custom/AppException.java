package com.jphilips.shared.exceptions.custom;

import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class AppException extends BaseException{

    public AppException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public AppException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
