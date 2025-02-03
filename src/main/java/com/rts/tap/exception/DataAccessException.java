package com.rts.tap.exception;

@SuppressWarnings("serial")
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}