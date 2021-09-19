package com.adobe.prj.util;

import java.io.Serializable;

import com.adobe.prj.entity.EmployeeRoles;

public class AuthenticationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String jwt;
	private final int id;
	private final String email;
	private final String firstName;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getJwt() {
		return jwt;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public EmployeeRoles getRole() {
		return role;
	}

	public Boolean getIsPasswordTemp() {
		return isPasswordTemp;
	}

	private final String lastName;
	private final EmployeeRoles role;
	private final Boolean isPasswordTemp;

	public AuthenticationResponse(String jwt, int id, String email, String firstName, String lastName,
			EmployeeRoles role, Boolean isPasswordTemp) {
		super();
		this.jwt = jwt;
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.isPasswordTemp = isPasswordTemp;
	}

}
