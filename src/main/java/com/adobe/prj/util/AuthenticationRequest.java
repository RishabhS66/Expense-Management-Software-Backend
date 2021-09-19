package com.adobe.prj.util;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class AuthenticationRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Email(message = "email is in wrong format")
	@NotEmpty(message = "Please provide your email")
	private String username;

	@NotEmpty(message = "Please enter password")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// need default constructor for JSON Parsing
	public AuthenticationRequest() {

	}

	public AuthenticationRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}
}
