package com.rts.tap.exception;

@SuppressWarnings("serial")
public class NoAssignedRecruitersFound extends RuntimeException{

	public NoAssignedRecruitersFound(String msg) {
		super(msg);
	}

}
