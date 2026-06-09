package com.manycoreapis.sdk.core;

public class BusinessException extends AholoException {
    private final String cmdCode;

    public BusinessException(String message, String cmdCode, Object body) {
        super(message, null, null, null, body);
        this.cmdCode = cmdCode;
    }

    public String getCmdCode() { return cmdCode; }
}
