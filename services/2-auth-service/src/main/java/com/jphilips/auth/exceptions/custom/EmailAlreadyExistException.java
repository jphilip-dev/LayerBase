package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class EmailAlreadyExistException extends AppException {
    public EmailAlreadyExistException(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
