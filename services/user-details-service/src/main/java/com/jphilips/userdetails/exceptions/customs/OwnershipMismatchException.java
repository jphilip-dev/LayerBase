package com.jphilips.userdetails.exceptions.customs;

import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class OwnershipMismatchException extends AppException {
    public OwnershipMismatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
