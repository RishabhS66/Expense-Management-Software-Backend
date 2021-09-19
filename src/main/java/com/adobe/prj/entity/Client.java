package com.adobe.prj.entity;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import com.adobe.prj.util.AddressJsonConverter;

/**
 * This is the class containing details of Client added by the employee.
 */
@Entity
@Table(name = "clients")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "client_id")
	private int id;

	@NotEmpty(message = "full name cannot be empty")
	@Column(name = "full_name", nullable = false)
	private String fullName;

	@NotEmpty(message = "nick name cannot be empty")
	@Column(name = "nick_name", unique = true, nullable = false)
	private String nickName;

	@NotEmpty(message = "email cannot be empty")
	@Email(message = "email is in wrong format")
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Valid
	@NotNull(message = "Need to provide address")
	@Convert(converter = AddressJsonConverter.class)
	@Column(name = "address", length = 1000)
	private Address address;

	@NotEmpty(message = "website cannot be empty")
	@URL(protocol = "https", message = "need https URL")
	@Column(name = "website", nullable = false, unique = true)
	private String website = "";

	@Column(name = "bill_details")
	private String billDetails = "";

	/**
	 * @return Integer unique project id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return String Client's full name.
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName Client's full name.
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return String Client's unique nick name.
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName Client's unique nick name.
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return String Client's email id.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email Client's email id.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Address Client's full address.
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address Client's full address.
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @param updates Key-value pairs for attributes to update in address.
	 */
	public void updateAddress(Map<String, Object> updates) {
		PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(this.address);
		for (String k : updates.keySet()) {
			myAccessor.setPropertyValue(k, updates.get(k));
		}
	}

	/**
	 * @return String Client's website url.
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website Client's website url.
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return String Client's full name.
	 */
	public String getBillDetails() {
		return billDetails;
	}

	/**
	 * @param full name Client's full name.
	 */
	public void setBillDetails(String billDetails) {
		this.billDetails = billDetails;
	}

	/**
	 * Default Constructor for the class.
	 */
	public Client() {

	}

	/**
	 * Constructor for the class.
	 *
	 * @param id          The unique Client ID(PrimaryKey for Database Table)
	 * @param fullName    full name of the client
	 * @param nickName    unique nickname for client
	 * @param email       email id of the client
	 * @param address     Address of the client
	 * @param website     website url of client
	 * @param billDetails details of the bill generated
	 */
	public Client(int id, String fullName, String nickName, String email, Address address, String website,
			String billDetails) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.nickName = nickName;
		this.email = email;
		this.address = address;
		this.website = website;
		this.billDetails = billDetails;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", fullName=" + fullName + ", nickName=" + nickName + ", email=" + email
				+ ", address=" + address + ", website=" + website + ", billDetails=" + billDetails + "]";
	}

}
