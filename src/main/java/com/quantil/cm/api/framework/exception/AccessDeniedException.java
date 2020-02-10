package com.quantil.cm.api.framework.exception;

public class AccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String code = "AccessDenied";
    public AccessDeniedException(String message) {
        super(message);
    }
    public String getCode() {
        return code;
    }
    
}
