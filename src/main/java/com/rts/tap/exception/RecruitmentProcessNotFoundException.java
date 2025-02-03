package com.rts.tap.exception;

@SuppressWarnings("serial")
public class RecruitmentProcessNotFoundException extends RuntimeException {
    public RecruitmentProcessNotFoundException(String message) {
        super(message);
    }
}