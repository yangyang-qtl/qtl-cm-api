package com.quantil.cm.api.framework.exception;

import com.quantil.cm.api.constant.Constants;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 9189401830978813377L;
    private final String code = Constants.ERROR_401;
    public UnauthorizedException(String message) {
        super(message);
    }
    public String getCode() {
        return code;
    }
}
