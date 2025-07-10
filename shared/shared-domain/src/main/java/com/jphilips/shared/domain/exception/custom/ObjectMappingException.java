package com.jphilips.shared.domain.exception.custom;

import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class ObjectMappingException extends AppException{
    public ObjectMappingException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public ObjectMappingException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
