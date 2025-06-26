package com.jphilips.auth.exceptions.custom;

import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class MissingJwtException extends AppException {
    public MissingJwtException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
