package com.adobe.prj.entity;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

/**
 * This is the class containing details of Address added by the employee.
 */
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Address Line 1 cannot be empty")
	private String addressLine1;

	private String addressLine2;

	@NotEmpty(message = "City cannot be empty")
	private String city;

	@NotEmpty(message = "State cannot be empty")
	private String state;

	@NotEmpty(message = "Country cannot be empty")
	private String country;

	@NotEmpty(message = "Zipcode cannot be empty")
	private String zipcode;

	@NotEmpty(message = "Telephone 1 cannot be empty")
	private String telephone1;

	private String telephone2;

	private String fax;

	/**
	 * @return String Line 1 for full address.
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @param addressLine1 Line 1 for full address.
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return String Line 1 for full address.
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @param addressLine2 optional Line 2 for full address.
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return String optional Line 2 for full address.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city name.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return String state name.
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state name.
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return String country name.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country name.
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return String zipcode for address.
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode of address.
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return String telephone no. 1.
	 */
	public String getTelephone1() {
		return telephone1;
	}

	/**
	 * @param telephone1 telephone no. 1.
	 */
	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	/**
	 * @return String optional telephone no. 2.
	 */
	public String getTelephone2() {
		return telephone2;
	}

	/**
	 * @param telephone2 optional telephone no. 2.
	 */
	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	/**
	 * @return String fax number.
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax number.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Constructor for the class.
	 *
	 * @param addressLine1 Line 1 for the address
	 * @param addressLine2 optional line 2 for address
	 * @param city         city name
	 * @param state        state name
	 * @param country      country name
	 * @param zipcode      zipcode
	 * @param telehone1    telephone no. 1
	 * @param telehone2    optional telephone no. 2
	 * @param fax          fax number
	 */
	public Address(String addressLine1, String addressLine2, String city, String state, String country, String zipcode,
			String telephone1, String telephone2, String fax) {
		super();
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipcode = zipcode;
		this.telephone1 = telephone1;
		this.telephone2 = telephone2;
		this.fax = fax;
	}

	/**
	 * Default constructor for the class.
	 */
	public Address() {

	}

	/**
	 * @return String Class attributes as key-value pairs.
	 */
	@Override
	public String toString() {
		return "Address [addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", city=" + city
				+ ", state=" + state + ", country=" + country + ", zipcode=" + zipcode + ", telephone1=" + telephone1
				+ ", telephone2=" + telephone2 + ", fax=" + fax + "]";
	}

}
