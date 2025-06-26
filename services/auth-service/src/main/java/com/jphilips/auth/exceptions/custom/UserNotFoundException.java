package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotFoundException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
