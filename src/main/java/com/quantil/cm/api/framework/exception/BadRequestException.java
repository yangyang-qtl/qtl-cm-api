package com.quantil.cm.api.framework.exception;

import com.quantil.cm.api.constant.Constants;

public class BadRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private final String code = Constants.ERROR_400_BAD_REQUEST;
    public BadRequestException(String message) {
        super(message);
    }
    public String getCode() {
        return code;
    }
}
