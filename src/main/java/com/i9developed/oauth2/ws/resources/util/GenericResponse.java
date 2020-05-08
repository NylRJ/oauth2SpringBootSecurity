package com.i9developed.oauth2.ws.resources.util;

import java.io.Serializable;

public class GenericResponse implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String erro;
	
	public GenericResponse(String message) {
		super();
		this.message = message;
		
	}
	public GenericResponse(String message, String erro) {
		this(message);
		this.erro = erro;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getErro() {
		return erro;
	}


	public void setErro(String erro) {
		this.erro = erro;
	}
	
	

}
