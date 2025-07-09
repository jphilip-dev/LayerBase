package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class PasswordMismatchException extends AppException {
    public PasswordMismatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
