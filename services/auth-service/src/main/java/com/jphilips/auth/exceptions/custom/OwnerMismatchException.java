package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class OwnerMismatchException extends AppException {
    public OwnerMismatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
