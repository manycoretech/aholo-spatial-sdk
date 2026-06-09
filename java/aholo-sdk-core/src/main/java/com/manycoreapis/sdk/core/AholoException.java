package com.manycoreapis.sdk.core;

public class AholoException extends RuntimeException {
    private final Integer statusCode;
    private final String status;
    private final String bizCode;
    private final Object body;

    public AholoException(String message, Integer statusCode, String status, String bizCode, Object body) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
        this.bizCode = bizCode;
        this.body = body;
    }

    public AholoException(String message) {
        this(message, null, null, null, null);
    }

    public AholoException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.status = null;
        this.bizCode = null;
        this.body = null;
    }

    public Integer getStatusCode() { return statusCode; }
    public String getStatus() { return status; }
    public String getBizCode() { return bizCode; }
    public Object getBody() { return body; }
}
