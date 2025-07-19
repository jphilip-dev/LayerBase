package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class OtpMismatchException extends AppException {
    public OtpMismatchException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
