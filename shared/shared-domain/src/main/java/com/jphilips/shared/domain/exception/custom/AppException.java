package com.jphilips.shared.domain.exception.custom;


import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class AppException extends BaseException{

    public AppException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public AppException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
