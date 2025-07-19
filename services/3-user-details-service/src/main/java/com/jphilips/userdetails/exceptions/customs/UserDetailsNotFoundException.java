package com.jphilips.userdetails.exceptions.customs;

import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.errorcode.BaseErrorCode;

public class UserDetailsNotFoundException extends AppException {
    public UserDetailsNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
