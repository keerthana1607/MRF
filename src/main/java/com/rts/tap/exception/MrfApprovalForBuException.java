package com.rts.tap.exception;

@SuppressWarnings("serial")
public class MrfApprovalForBuException extends RuntimeException {

	public MrfApprovalForBuException(String message, Throwable cause) {
		super(message, cause);
	}

	public MrfApprovalForBuException(String message) {
		super(message);
	}

}
