package com.jphilips.auth.exceptions.custom;

import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphiilips.shared.domain.exception.errorcode.BaseErrorCode;

public class UserInactiveException extends AppException {
    public UserInactiveException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
