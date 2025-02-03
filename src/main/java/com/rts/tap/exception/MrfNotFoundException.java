package com.rts.tap.exception;

@SuppressWarnings("serial")
public class MrfNotFoundException extends RuntimeException{
	
	String message;

	public MrfNotFoundException(String message) {
		super(message);
	}
}
