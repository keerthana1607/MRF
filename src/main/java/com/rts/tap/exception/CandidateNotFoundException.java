package com.rts.tap.exception;

@SuppressWarnings("serial")
public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException(String message) {
        super(message);
    }

}