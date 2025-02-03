package com.rts.tap.exception;

@SuppressWarnings("serial")
public class CandidateSaveException extends RuntimeException {
    public CandidateSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}