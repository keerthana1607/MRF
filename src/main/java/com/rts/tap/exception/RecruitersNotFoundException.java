package com.rts.tap.exception;

@SuppressWarnings("serial")
public class RecruitersNotFoundException extends RuntimeException {
	
	String message;

	public RecruitersNotFoundException(String message) {
		super(message);
	}
}
