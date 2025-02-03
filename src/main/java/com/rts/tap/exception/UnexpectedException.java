package com.rts.tap.exception;

@SuppressWarnings("serial")
public class UnexpectedException extends RuntimeException {
    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
