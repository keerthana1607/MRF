package com.rts.tap.exception;

@SuppressWarnings("serial")
public class JobDescriptionTitleNotFoundException extends RuntimeException{
	
	String message;

	public JobDescriptionTitleNotFoundException(String message) {
		super(message);
	}
}
