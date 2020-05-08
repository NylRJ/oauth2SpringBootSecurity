package com.i9developed.oauth2.ws.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.i9developed.oauth2.ws.domain.Role;
import com.i9developed.oauth2.ws.domain.User;


public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String id;
	private String firtName;
	private String lastName;
	private String email;
	
	private String password;
	private Boolean enable;
	
	private List<Role> roles = new ArrayList<>();

	
	public UserDTO() {
	}

	public UserDTO(User user) {
		super();
		this.id = user.getId();
		this.firtName = user.getFirtName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.enable = user.getEnable();
		this.roles = user.getRoles();
		
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
	

	
}
