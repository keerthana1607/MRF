package com.rts.tap.exception;

@SuppressWarnings("serial")
public class OfferApprovalNotFoundException extends RuntimeException{

	String message;

	public OfferApprovalNotFoundException(String message) {
		super(message);
	}
}
