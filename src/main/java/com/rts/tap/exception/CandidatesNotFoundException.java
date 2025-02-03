package com.rts.tap.exception;

@SuppressWarnings("serial")
public class CandidatesNotFoundException extends RuntimeException {
    public CandidatesNotFoundException(String message) {
        super(message);
    }
}
