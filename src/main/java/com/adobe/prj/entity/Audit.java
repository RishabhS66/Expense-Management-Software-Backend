package com.adobe.prj.entity;

import java.util.Date;

public class Audit {
	private boolean isDeleted;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;

	Audit(String createdBy, String updatedBy) {
		this.isDeleted = false;
		this.createdDate = new Date();
		this.updatedDate = new Date();
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;

	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
