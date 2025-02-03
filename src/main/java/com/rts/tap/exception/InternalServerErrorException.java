package com.rts.tap.exception;

@SuppressWarnings("serial")
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}