package com.i9developed.oauth2.ws.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VerificationToken implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int EXPIRATION = 60 * 24;

	@Id
	private String id;
	private String token;

	@DBRef(lazy = true)
	private User user;
	private Date expiryDate;

	public VerificationToken() {

	}

	public VerificationToken(final String token) {
		this();
		this.token = token;

		this.setExpiryDate(calculateExpiryDate(EXPIRATION));

	}

	public VerificationToken(final String token, final User user) {
		this(token);
		this.user = user;
		this.setExpiryDate(calculateExpiryDate(EXPIRATION));
	}
	
	public void updateToken(final String token) {
		 this.token = token;
		 this.expiryDate = calculateExpiryDate(EXPIRATION);
		}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
		VerificationToken other = (VerificationToken) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private Date calculateExpiryDate(final int expiryTimeInMinutes) {

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());

	}

	

}
