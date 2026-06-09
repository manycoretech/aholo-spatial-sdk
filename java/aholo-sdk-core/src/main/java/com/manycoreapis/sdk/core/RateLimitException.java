package com.manycoreapis.sdk.core;

public class RateLimitException extends AholoException {
    public RateLimitException(String message, Integer statusCode, String status, String bizCode, Object body) {
        super(message, statusCode, status, bizCode, body);
    }
}
