package com.jphilips.shared.spring.exception;

import com.jphiilips.shared.domain.exception.errorcode.BaseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodeAdapter{

    public HttpStatus covertRawStatus(BaseErrorCode baseErrorCode) {
        HttpStatus httpStatus;
        try {
            httpStatus = HttpStatus.valueOf(baseErrorCode.getStatusCode());
        }catch (Exception e){
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return httpStatus;
    }

}
