package com.adobe.prj.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_id", nullable = false)
	private int id;

	@Email(message = "email is in wrong format")
	@NotEmpty(message = "email cannot be empty")
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "first_name", nullable = false)
	@NotEmpty(message = "first name cannot be empty")
	private String firstName;

	@Column(name = "last_name", nullable = false)
	@NotEmpty(message = "last name cannot be empty")
	private String lastName;

	@Column(name = "password")
	@NotEmpty(message = "password cannot be empty")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(name = "role")
	private EmployeeRoles role;

	@Column(name = "is_password_temp")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Boolean isPasswordTemp = true;

	@ManyToOne
	@JoinColumn(name = "manager_fk")

	private Employee manager;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "teamMembers")
//	@JoinTable(name = "employee_projects", joinColumns = @JoinColumn(name = "emp_id"), inverseJoinColumns = @JoinColumn(name = "prj_id"))
	@JsonIgnoreProperties({ "teamMembers", "attachments" })
	private Set<Project> projects;

	public Employee() {

	}

	public Employee(int id, String email, String firstName, String lastName, String password, EmployeeRoles role,
			Boolean isPasswordTemp, Employee manager, Set<Project> projects) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.role = role;
		this.isPasswordTemp = isPasswordTemp;
		this.manager = manager;
		this.projects = projects;
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

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EmployeeRoles getRole() {
		return role;
	}

	public void setRole(EmployeeRoles role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getIsPasswordTemp() {
		return isPasswordTemp;
	}

	public void setIsPasswordTemp(Boolean isPasswordTemp) {
		this.isPasswordTemp = isPasswordTemp;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

}
