package com.rts.tap.exception;

@SuppressWarnings("serial")
public class AddMrfException extends RuntimeException {

	public AddMrfException(String message, Throwable cause) {
		super(message, cause);
	}

	public AddMrfException(String message) {
		super(message);
	}
}
