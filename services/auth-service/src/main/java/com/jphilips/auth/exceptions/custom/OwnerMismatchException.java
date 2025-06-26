package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class OwnerMismatchException extends AppException {
    public OwnerMismatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public OwnerMismatchException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
