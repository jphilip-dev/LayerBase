package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class UserInactiveException extends AppException {
    public UserInactiveException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
