package com.adobe.prj.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.prj.entity.Document;
import com.adobe.prj.entity.Employee;
import com.adobe.prj.entity.EmployeeRoles;
import com.adobe.prj.entity.Expense;
import com.adobe.prj.entity.ExpenseStatus;
import com.adobe.prj.entity.Project;
import com.adobe.prj.exceptions.BadRequestException;
import com.adobe.prj.exceptions.NotFoundException;
import com.adobe.prj.service.ExpenseService;
import com.adobe.prj.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("api/expenses")
@Api(value = "ExpenseController", description = "REST APIs related to Expense Entity!!!!")
public class ExpenseController {
	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private ProjectService projectService;

	@ApiOperation(value = "get total expenses for a project ", response = Map.class, tags = "getExpenseTotal")
	@GetMapping("/projectAmount/{pid}")
	public @ResponseBody Map<String, Double> getExpenseTotal(@PathVariable("pid") int pid) {
		return expenseService.getExpenseTotal(pid);
	}

	Employee getEmployee() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Employee employee = (Employee) auth.getPrincipal();
		return employee;
	}

	@ApiOperation(value = "get an expense by ID", response = Expense.class, tags = "getExpense")
	@GetMapping("/{eid}")
	public @ResponseBody Expense getExpense(@PathVariable("eid") int id)
			throws AccessDeniedException, NotFoundException {
		Expense e = expenseService.getExpense(id);
		Employee emp = getEmployee();
		if (e.getEmployee().getId() == emp.getId() || e.getProject().getProjectManager().getId() == emp.getId()
				|| emp.getRole() == EmployeeRoles.ADMIN)
			return e;
		throw new AccessDeniedException("You don't have access to this expense");
	}

	@ApiOperation(value = "add an expense ", response = Expense.class, tags = "addExpense")
	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public @ResponseBody Expense addExpense(@RequestBody @Valid Expense e)
			throws AccessDeniedException, NotFoundException {
		Employee emp = getEmployee();
		e.setEmployee(emp);
		e.setStatus(ExpenseStatus.SUBMITTED);
		Project p = projectService.getProject(e.getProject().getId());

		if (emp.getId() == p.getProjectManager().getId()) {
			return expenseService.addExpense(e);
		}
		for (Project project : emp.getProjects())
			if (e.getProject().getId() == project.getId())
				return expenseService.addExpense(e);
		throw new AccessDeniedException("You are not part of the project");
	}

	@ApiOperation(value = "get expenses by a project ", response = Iterable.class, tags = "getExpenseByProject")
	@GetMapping("/projects/{pid}")
	public @ResponseBody List<Expense> getExpenseByProject(@PathVariable("pid") int id)
			throws NotFoundException, AccessDeniedException {
		Employee emp = getEmployee();
		for (Project project : emp.getProjects())
			if (id == project.getId() && emp.getRole() == EmployeeRoles.PRJ_MANAGER)
				return expenseService.getExpenseByProject(id);
		throw new AccessDeniedException("You are not part of the project");
	}

	@ApiOperation(value = "get an employee expenses", response = Iterable.class, tags = "getMyExpense")
	@GetMapping()
	public @ResponseBody List<Expense> getMyExpense(@RequestParam(name = "page", defaultValue = "-1") int page,
			@RequestParam(name = "size", defaultValue = "-1") int size) throws NotFoundException {
		Employee emp = getEmployee();
		return expenseService.getExpenseByEmplyoeeId(emp.getId());
	}

	@ApiOperation(value = "get expense approvals", response = Iterable.class, tags = "getApprovalsByStatus")
	@GetMapping("/approvals/{status}")
	public @ResponseBody List<Expense> getApprovalsByStatus(@RequestParam(name = "page", defaultValue = "-1") int page,
			@RequestParam(name = "size", defaultValue = "-1") int size, @PathVariable("status") int status)
			throws NotFoundException {
		Employee emp = getEmployee();
		if (emp.getRole() == EmployeeRoles.PRJ_MANAGER) {
			List<Expense> expenses = new ArrayList<>();
			List<Project> all_projects = projectService.getAllProjects();
			for (Project p : all_projects)
				if (p.getProjectManager().getId() == emp.getId())
					expenses.addAll(expenseService.getExpenseByStatusforProject(p.getId(), status));
			return expenses;
		} else if (emp.getRole() == EmployeeRoles.ADMIN)
			return expenseService.getExpenseByStatus(status);
		return new ArrayList<Expense>();
	}

	
	@ApiOperation(value = "update expense approvals", response = Expense.class, tags = "updateExpenseStatus")
	@RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Expense updateExpenseStatus(@RequestBody Map<String, Object> updates,
			@PathVariable("id") int id) throws NotFoundException, AccessDeniedException {
		Expense e = expenseService.getExpense(id);
		Employee emp = getEmployee();
		if (((emp.getRole() == EmployeeRoles.PRJ_MANAGER && e.getProject().getProjectManager().getId() == emp.getId()
				&& e.getEmployee().getId() != emp.getId()) || emp.getRole() == EmployeeRoles.ADMIN)
				&& e.getStatus() == ExpenseStatus.SUBMITTED) {

			return expenseService.updateExpenseStatus(updates, e);
		}
		throw new AccessDeniedException("Employee doesn't have access to update it");
	}

	@ApiOperation(value = "update expense", response = Expense.class, tags = "updateExpense")
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Expense updateExpense(@RequestBody Map<String, Object> updates, @PathVariable("id") int id)
			throws NotFoundException, AccessDeniedException, BadRequestException {
		Expense e = expenseService.getExpense(id);
		Employee emp = getEmployee();
		// Non Modifiable fields check
		if (updates.containsKey("status") || updates.containsKey("employee") || updates.containsKey("project"))
			throw new AccessDeniedException("Non Modifiable fields given");
//		if(updates.containsKey("projects")) {
//			boolean isProjectMember = false;
//			for(Project project:emp.getProjects()) {
//				if(updates.get("projects").toString().equals(String.valueOf(project.getId())))
//					isProjectMember = true;
//			}
//			if(isProjectMember)
//				throw new AccessDeniedException("Employee is not part of the project");
//		}
		else if (emp.getId() == e.getEmployee().getId() && e.getStatus() == ExpenseStatus.SUBMITTED)
			return expenseService.updateExpense(updates, e);
		throw new AccessDeniedException("Employee doesn't have access to update it");
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "delete an expense", tags = "deleteExpense")
	public void deleteExpense(@PathVariable("id") int id) throws NotFoundException, AccessDeniedException {
		Employee emp = getEmployee();
		if (emp.getId() == expenseService.getExpense(id).getEmployee().getId()
				&& expenseService.getExpense(id).getStatus() == ExpenseStatus.SUBMITTED)
			expenseService.deleteExpense(id);
		else
			throw new AccessDeniedException("Employee doesn't have access to delete the expense");
	}

	@ApiOperation(value = "delete expenses", tags = "deleteExpenses")
	@DeleteMapping()
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteExpenses(@Valid @RequestBody List<Integer> ids) throws NotFoundException, AccessDeniedException {
		Employee emp = getEmployee();
		for (int id : ids)
			if (emp.getId() != expenseService.getExpense(id).getEmployee().getId()
					&& expenseService.getExpense(id).getStatus() == ExpenseStatus.SUBMITTED)
				throw new AccessDeniedException("Employee doesn't have access to delete one of the expenses");
		expenseService.deleteExpenses(ids);
	}

}
