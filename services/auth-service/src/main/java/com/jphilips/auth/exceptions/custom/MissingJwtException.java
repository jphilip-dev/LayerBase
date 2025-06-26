package com.jphilips.auth.exceptions.custom;

import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphiilips.shared.domain.exception.errorcode.BaseErrorCode;

public class MissingJwtException extends AppException {
    public MissingJwtException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
