package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class EmailAlreadyExistException extends AppException {
    public EmailAlreadyExistException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public EmailAlreadyExistException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
