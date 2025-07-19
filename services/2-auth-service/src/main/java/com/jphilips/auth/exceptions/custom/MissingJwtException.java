package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class MissingJwtException extends AppException {
    public MissingJwtException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
