package com.rts.tap.exception;

@SuppressWarnings("serial")
public class BadInputException extends RuntimeException {
    public BadInputException(String message, Throwable cause) {
        super(message, cause);
    }
}