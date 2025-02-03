package com.rts.tap.exception;

@SuppressWarnings("serial")
public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}