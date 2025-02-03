package com.rts.tap.exception;

@SuppressWarnings("serial")
public class ApproverLevelNotFoundException extends RuntimeException {
    String message;

    public ApproverLevelNotFoundException(String message) {
        super(message);
        
    }
}