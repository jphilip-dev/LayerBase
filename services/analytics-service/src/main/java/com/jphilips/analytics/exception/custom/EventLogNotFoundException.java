package com.jphilips.analytics.exception.custom;

import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphiilips.shared.domain.exception.errorcode.BaseErrorCode;

public class EventLogNotFoundException extends AppException {
    public EventLogNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public EventLogNotFoundException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
