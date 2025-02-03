package com.rts.tap.exception;

@SuppressWarnings("serial")
public class RequirementCountNotFoundException extends RuntimeException{
	
	String message;

	public RequirementCountNotFoundException(String message) {
		super(message);
	}
}
