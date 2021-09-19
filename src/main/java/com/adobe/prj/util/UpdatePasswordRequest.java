package com.adobe.prj.util;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class UpdatePasswordRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Please enter your old password")
	private String old_password;

	public String getOld_password() {
		return old_password;
	}

	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	public String getConfirm_password() {
		return confirm_password;
	}

	public void setConfirm_password(String confirm_password) {
		this.confirm_password = confirm_password;
	}

	@NotEmpty(message = "Please enter your new password")
	private String new_password;

	@NotEmpty(message = "Please confirm your new password")
	private String confirm_password;

	public UpdatePasswordRequest(String old_password, String new_password, String confirm_password) {
		super();
		this.old_password = old_password;
		this.new_password = new_password;
		this.confirm_password = confirm_password;
	}

}
