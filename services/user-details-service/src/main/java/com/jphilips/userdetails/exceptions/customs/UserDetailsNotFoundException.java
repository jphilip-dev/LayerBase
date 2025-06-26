package com.jphilips.userdetails.exceptions.customs;

import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.errorcode.BaseErrorCode;

public class UserDetailsNotFoundException extends AppException {
    public UserDetailsNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
