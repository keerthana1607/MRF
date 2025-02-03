package com.rts.tap.exception;

@SuppressWarnings("serial")
public class RecruitingManagerNotFoundException extends RuntimeException {
	
	String message;

	public RecruitingManagerNotFoundException(String message) {
		super(message);
	}
}
