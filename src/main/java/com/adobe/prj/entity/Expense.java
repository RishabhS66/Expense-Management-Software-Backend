package com.adobe.prj.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.adobe.prj.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "expenses")
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "expense_id")
	private Integer expenseId;

	@ManyToOne
	@JoinColumn(name = "employee_fk", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "project_fk", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Project project;

	@NotBlank(message = "Name cannot be empty")
	private String name;

	@Column(nullable = false)
	@NotEmpty(message = "Currency cannot be empty")
	private String currency;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date date = new Date();

	@Column(nullable = false)
	private Boolean billable;

	@Column(nullable = false)
	private Boolean reimburseable;

	@Column(nullable = false)
	@NotNull(message = "Amount cannot be null")
	private double amount;

	@Size(max = 500, message = "Description should not be longer than 500 characters")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private String taxZone = Constants.DEFAULT_TAX_ZONE;

	private Double tax;

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
	@JoinColumn(name = "expense_fk")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnoreProperties("file")
	private Set<Document> attachments;

	@NotNull(message = "status cannot be null")
	private ExpenseStatus status;

	@Column(name = "payment_method", nullable = false)
	private String paymentMethod;

	public Expense(Integer expenseId, Employee employee, Project project, String name, String currency, Date date,
			Boolean billable, Boolean reimburseable, double amount, String description, String taxZone, Double tax,
			Set<Document> attachments, ExpenseStatus status, String paymentMethod) {
		super();
		this.expenseId = expenseId;
		this.employee = employee;
		this.project = project;
		this.name = name;
		this.currency = currency;
		this.date = date;
		this.billable = billable;
		this.reimburseable = reimburseable;
		this.amount = amount;
		this.description = description;
		this.taxZone = taxZone;
		this.tax = tax;
		this.attachments = attachments;
		this.status = status;
		this.paymentMethod = paymentMethod;
	}

	public Expense() {
		super();
	}

	public Set<Document> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Document> attachments) {
		this.attachments = attachments;
	}

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public Boolean getReimburseable() {
		return reimburseable;
	}

	public void setReimburseable(Boolean reimburseable) {
		this.reimburseable = reimburseable;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTaxZone() {
		return taxZone;
	}

	public void setTaxZone(String taxZone) {
		this.taxZone = taxZone;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public ExpenseStatus getStatus() {
		return status;
	}

	public void setStatus(ExpenseStatus status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Override
	public String toString() {
		return "Expense [expenseId=" + expenseId + ", employee=" + employee + ", project=" + project + ", name=" + name
				+ ", currency=" + currency + ", date=" + date + ", billable=" + billable + ", reimburseable="
				+ reimburseable + ", amount=" + amount + ", taxZone=" + taxZone + ", tax=" + tax + ", status=" + status
				+ ", paymentMethod=" + paymentMethod + "]";
	}

}
