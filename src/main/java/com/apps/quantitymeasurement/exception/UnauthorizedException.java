package com.apps.quantitymeasurement.exception;

public class UnauthorizedException extends RuntimeException{
	
	private static final long serialVersionID = 1L;
	
	public UnauthorizedException(String msg) {
		super(msg);
	}
}
