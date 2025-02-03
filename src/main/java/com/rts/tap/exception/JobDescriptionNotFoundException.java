package com.rts.tap.exception;

@SuppressWarnings("serial")
public class JobDescriptionNotFoundException extends RuntimeException {
	
	String message;

	public JobDescriptionNotFoundException(String message) {
		super(message);
	}
}
