package com.quantil.cm.api.framework.exception;

import com.quantil.cm.api.constant.Constants;

public class NoFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String code = Constants.ERROR_404;
    public NoFoundException(String message) {
        super(message);
    }
    public String getCode() {
        return code;
    }
}