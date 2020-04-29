package com.i9developed.oauth2.ws.dto;

import java.io.Serializable;

import com.i9developed.oauth2.ws.domain.User;


public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String id;
	private String firtName;
	private String lastName;
	private String email;

	public UserDTO() {
	}

	public UserDTO(User user) {
		super();
		this.id = user.getId();
		this.firtName = user.getFirtName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirtName() {
		return firtName;
	}

	public void setFirtName(String firtName) {
		this.firtName = firtName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
