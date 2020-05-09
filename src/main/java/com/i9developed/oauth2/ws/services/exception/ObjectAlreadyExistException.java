package com.i9developed.oauth2.ws.services.exception;

public class ObjectAlreadyExistException  extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public ObjectAlreadyExistException(String msg) {
		super(msg);
	}
}
