package com.quantil.cm.api.framework.exception;

import com.quantil.cm.api.constant.Constants;

public class InternalException extends RuntimeException {
    private static final long serialVersionUID = 9189401830978813377L;
    private final String code = Constants.ERROR_500;
    public InternalException(String message) {
        super(message);
    }
    public InternalException() {
        
    }
    public String getCode() {
        return code;
    }
}
