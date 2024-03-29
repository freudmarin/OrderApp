package com.marindulja.orderapp.exception;

import org.springframework.http.HttpStatus;

public class OrderAppException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public OrderAppException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
