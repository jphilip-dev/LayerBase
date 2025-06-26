package com.jphilips.userdetails.exceptions.customs;

import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphiilips.shared.domain.exception.errorcode.BaseErrorCode;

public class UserDetailsNotFoundException extends AppException {
    public UserDetailsNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
