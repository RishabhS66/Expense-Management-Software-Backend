package com.adobe.prj.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This is the class containing details of Project added by the employee.
 */
@Entity
@Table(name = "projects")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prj_id")
	private int id;

	@NotNull(message = "Client is required")
	@ManyToOne
	@JoinColumn(name = "client_fk")
	private Client client;

	@Size(max = 500, message = "Description should not be longer than 500 characters")
	private String description = "";

	@Column(name = "active")
	private boolean isActive;

	@Column(name = "soon_due")
	private boolean isSoonDue;

	@NotNull(message = "ProjectManager is required")
	@ManyToOne
	@JoinColumn(name = "project_manager_fk")
	@JsonIgnoreProperties("projects")
	private Employee projectManager;

	@NotBlank(message = "ProjectName cannot be blank")
	@Column(name = "project_name", unique = true)
	private String projectName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "start_date")
	private Date startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "end_date")
	private Date endDate;

	@NotNull(message = "Status cannot be null")
	private ProjectStatus status;

	@ManyToMany
	@JoinTable(name = "employee_projects", joinColumns = @JoinColumn(name = "prj_id"), inverseJoinColumns = @JoinColumn(name = "emp_id"))
	@JsonIgnoreProperties("projects")
	private Set<Employee> teamMembers = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
	@JoinColumn(name = "project_fk")
	@JsonIgnoreProperties("file")
	private Set<Document> attachments;

	/**
	 * Default Constructor for the class.
	 */
	public Project() {
		super();
	}

	/**
	 * Constructor for the class.
	 *
	 * @param id             The unique Project ID(PrimaryKey for Database Table).
	 * @param client         Client(foreign key) for the project
	 * @param projectManager Employee(foreign key) assigned as project manager for
	 *                       the project
	 * @param description    Describing all about the project in less than 500
	 *                       characters
	 * @param projectname    Unique name of the project
	 * @param startDate      When the project started
	 * @param endDate        When the project is expected to end or ended
	 * @param status         Current status/progress of the project
	 */
	public Project(int id, Client client, Employee projectManager, String description, String projectName,
			Date startDate, Date endDate, ProjectStatus status) {
		super();
		this.id = id;
		this.client = client;
		this.projectManager = projectManager;
		this.description = description;
		this.projectName = projectName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	/**
	 * @return Integer unique project id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return Client client associated to project.
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client associated to project.
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @return String about the project.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description about the project.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return boolean is the project currently active.
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive is the project currently active.
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return boolean is the project due in 15 days or less.
	 */
	public boolean isSoonDue() {
		return isSoonDue;
	}

	/**
	 * @param isSoonDue is the project due in 15 days or less.
	 */
	public void setSoonDue(boolean isSoonDue) {
		this.isSoonDue = isSoonDue;
	}

	/**
	 * @return Employee project manager for the project.
	 */
	public Employee getProjectManager() {
		return projectManager;
	}

	/**
	 * @param projectManager project manager for the project.
	 */
	public void setProjectManager(Employee projectManager) {
		this.projectManager = projectManager;
	}

	/**
	 * @return String unique name of the project.
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName unique name of the project.
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return Date starting date of project.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate starting date of project.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return Date ending date of project.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate ending date of project.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return ProjectStatus current status of work on project.
	 */
	public ProjectStatus getStatus() {
		return status;
	}

	/**
	 * @param status current status of work on project.
	 */
	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	/**
	 * @return Set<Employee> members of the project.
	 */
	public Set<Employee> getTeamMembers() {
		return teamMembers;
	}

	/**
	 * @param teamMembers members of the project.
	 */
	public void setTeamMembers(Set<Employee> teamMembers) {
		this.teamMembers = teamMembers;
	}

	/**
	 * @return Set<Document> list of attachments.
	 */
	public Set<Document> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments list of attachments.
	 */
	public void setAttachments(Set<Document> attachments) {
		this.attachments = attachments;
	}

}
