package com.jphilips.userdetails.exceptions.customs;


import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphiilips.shared.domain.exception.errorcode.BaseErrorCode;

public class OwnershipMismatchException extends AppException {
    public OwnershipMismatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
