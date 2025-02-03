package com.rts.tap.exception;

@SuppressWarnings("serial")
public class RecruiterNotFoundException extends RuntimeException {
	public RecruiterNotFoundException(String message) {
        super(message);
    }
}
