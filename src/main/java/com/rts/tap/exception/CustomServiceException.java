package com.rts.tap.exception;

@SuppressWarnings("serial")
public class CustomServiceException extends RuntimeException {
    public CustomServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}