package com.app.microservices.quantity_service.exception;

public class UnsupportedOperationException extends RuntimeException{

	static final long serialVersionUID = 1L;
	
	public UnsupportedOperationException(String msg) {
		super(msg);
	}
}