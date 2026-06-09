package com.manycoreapis.sdk.core;

public class AuthenticationException extends AholoException {
    public AuthenticationException(String message, Integer statusCode, String status, String bizCode, Object body) {
        super(message, statusCode, status, bizCode, body);
    }
}
