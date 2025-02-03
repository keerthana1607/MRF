package com.rts.tap.exception;

@SuppressWarnings("serial")
public class MRFUpdateException extends RuntimeException {
	public MRFUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public MRFUpdateException(String message) {
		super(message);
	}
}
