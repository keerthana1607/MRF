package com.rts.tap.exception;

@SuppressWarnings("serial")
public class MRFValidationException extends RuntimeException{
	public MRFValidationException(String message) {
        super(message);
    }
}
