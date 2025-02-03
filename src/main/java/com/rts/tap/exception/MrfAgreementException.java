package com.rts.tap.exception;

import java.io.IOException;

@SuppressWarnings("serial")
public class MrfAgreementException extends RuntimeException{

	String message;

	public MrfAgreementException(String message, IOException e) {
		super(message+" "+e.getMessage());
	}
	
	public MrfAgreementException(String message) {
		super(message);
	}
}
