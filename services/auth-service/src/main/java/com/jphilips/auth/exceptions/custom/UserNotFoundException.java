package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
