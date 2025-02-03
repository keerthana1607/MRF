package com.rts.tap.exception;

@SuppressWarnings("serial")
public class VendorsNotFoundException extends RuntimeException
{
	String message;

	public VendorsNotFoundException(String message) {
		super(message);
	}
}
