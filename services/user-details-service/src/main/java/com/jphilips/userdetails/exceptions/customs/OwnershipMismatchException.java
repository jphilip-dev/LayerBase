package com.jphilips.userdetails.exceptions.customs;


import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class OwnershipMismatchException extends AppException {
    public OwnershipMismatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
