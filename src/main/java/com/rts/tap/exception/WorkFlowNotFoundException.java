package com.rts.tap.exception;

@SuppressWarnings("serial")
public class WorkFlowNotFoundException extends RuntimeException {
    public WorkFlowNotFoundException(String message) {
        super(message);
    }
}
