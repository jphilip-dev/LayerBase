package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class PasswordMismatchException extends AppException {
    public PasswordMismatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
