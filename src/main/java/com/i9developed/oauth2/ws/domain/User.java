package com.i9developed.oauth2.ws.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.i9developed.oauth2.ws.dto.UserDTO;

@Document(collection ="users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String firtName;
	private String lastName;
	private String email;
	private String password;
	private Boolean enable;
	
	@DBRef(lazy = true)
	private List<Role> roles = new ArrayList<>();

	public User() {
	}
	
	public User(String firtName, String lastName, String email) {
		this();
		this.firtName = firtName;
		this.lastName = lastName;
		this.email = email;
	}

	public User(String id,String firtName, String lastName, String email) {
		this();
		this.id = id;
		this.firtName = firtName;
		this.lastName = lastName;
		this.email = email;
	}
	public User(String id ,String firtName, String lastName, String email, String password, Boolean enable) {
		this(id,firtName,lastName,email);
		this.password = password;
		this.enable = enable;
	}
	
	public User(User user) {
		super();
		this.id = user.getId();
		this.firtName = user.getFirtName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.roles = user.getRoles();
		
		
	}
	

	
	public User(UserDTO userDTO ) {
		this.id = userDTO.getId();
		this.firtName = userDTO.getFirtName();
		this.lastName = userDTO.getLastName();
		this.email = userDTO.getEmail();
		this.password = userDTO.getPassword();
		
		
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
	
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	


}
