package com.adobe.prj.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.adobe.prj.dao.ClientDao;
import com.adobe.prj.dao.DocumentDao;
import com.adobe.prj.dao.EmployeeDao;
import com.adobe.prj.dao.ProjectDao;
import com.adobe.prj.entity.Document;
import com.adobe.prj.entity.Employee;
import com.adobe.prj.entity.EmployeeRoles;
import com.adobe.prj.entity.Project;
import com.adobe.prj.exceptions.BadRequestException;
import com.adobe.prj.exceptions.NotFoundException;

/*
 * Team lead
 * Approval Type
 * Duration
 * Cost
 * Project Type
 * Template
 * Attachment
 * Project Summary
 */

@Service
public class ProjectService {

	@Autowired
	private ProjectDao projectDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private DocumentDao documentDao;

	public Project addProject(Project prj) throws BadRequestException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final Employee employee = (Employee) auth.getPrincipal();

		if (validateProject(prj)) {

			Set<Employee> mem = prj.getTeamMembers();
			mem.add(employee);
			prj.setTeamMembers(mem);

			return projectDao.save(prj);
		}
		return null;
	}

	public Project getProject(int prj_id) throws NotFoundException {
		if (isProjectAccessible(prj_id))
			return updateTags(projectDao.findById(prj_id).get());
		else
			throw new AccessDeniedException("Employee don't have access to Project");
	}

	@Transactional
	public List<Project> getAllProjects() {
		return projectDao.findAll();

	}

	@Transactional
	public List<Project> getProjectsByUser(int page, int size) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final Employee employee = (Employee) auth.getPrincipal();

		if (employee.getRole() == EmployeeRoles.ADMIN)
			return getAllProjects().stream().map(p -> updateTags(p)).collect(Collectors.toList());
		else {
			return projectDao.findByUser(employee.getId()).stream().map(p -> updateTags(p))
					.collect(Collectors.toList());
		}

	}

//	public List<Project> getProjectsByClient(int page, int size) {
//		
//			return projectDao.findByClient(employee.getId()).stream().map(p -> updateTags(p)).toList();
//
//	}

	public void deleteProject(int prj_id) throws NotFoundException {
		if (isProjectAccessible(prj_id))
			projectDao.deleteById(prj_id);
		else
			throw new AccessDeniedException("Employee don't have access to Project");

	}

	public void deleteProjects(List<Integer> ids) throws NotFoundException {
		for (int prj_id : ids) {
			if (!isProjectAccessible(prj_id))
				throw new AccessDeniedException("Employee don't have access to some Projects");
		}
		projectDao.deleteAllById(ids);
	}

	@SuppressWarnings("unchecked")
	@Valid
	public Project updateProject(Map<String, Object> updates, int prj_id)
			throws NotFoundException, BadRequestException {

		if (!isProjectAccessible(prj_id))
			throw new AccessDeniedException("Employee don't have access to the Project");

		Project prj = getProject(prj_id);

		if (updates.containsKey("isSoonDue") || updates.containsKey("isActive"))
			throw new BadRequestException("Cannot update tags isActive and isSoonDue");

		PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(prj);
		for (String k : updates.keySet()) {
			if (k == "client") {
				Map<String, Object> client = (Map<String, Object>) updates.get(k);
				prj.setClient(clientDao.getById((Integer) client.get("id")));

			} else if (k == "projectManager") {
				Map<String, Object> employee = (Map<String, Object>) updates.get(k);
				prj.setProjectManager(employeeDao.getById((Integer) employee.get("id")));

			} else if (k == "teamMembers") {
				Set<Employee> emps = new HashSet<Employee>();
				for (Map<String, Object> mp : (List<Map<String, Object>>) updates.get(k)) {
					emps.add(employeeDao.getById((Integer) mp.get("id")));
				}
				prj.setTeamMembers(emps);

			} else if (k == "attachments") {
				Set<Document> docs = new HashSet<>();
				for (Map<String, Object> doc : (List<Map<String, Object>>) updates.get(k)) {
					docs.add(documentDao.findById((Integer) doc.get("id")).get());

				}
				prj.setAttachments(docs);

			} else if (k == "startDate") {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
					prj.setStartDate(dateFormat.parse((String) updates.get(k)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (k == "endDate") {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
					prj.setEndDate(dateFormat.parse((String) updates.get(k)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				try {
					myAccessor.setPropertyValue(k, updates.get(k));
				} catch (BeansException e) {
					throw new BadRequestException("Error in updating property " + k);
				}
			}
		}

		if (validateProject(prj))
			return projectDao.save(prj);
		return null;

	}

	public Project updateTags(Project prj) {

		Date date = new Date();
		if (prj.getStartDate().before(date) && prj.getEndDate().after(date)) {
			prj.setActive(true);
		} else {
			prj.setActive(false);
		}

		long numOfmills = 24 * 60 * 60 * 1000;
		long timediff = Math.abs(prj.getEndDate().getTime() - date.getTime());
		int numOfDays = 15;

		if (timediff / numOfmills <= numOfDays) {
			prj.setSoonDue(true);
		} else {
			prj.setSoonDue(false);
		}

		return projectDao.save(prj);

	}

	public boolean isProjectAccessible(int prj_id) throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final Employee employee = (Employee) auth.getPrincipal();

		Project prj;
		try {
			prj = projectDao.findById(prj_id).get();
		} catch (Exception e1) {
			throw new NotFoundException("Project doesn't exist");
		}

		if (employee.getRole() == EmployeeRoles.ADMIN)
			return true;
		if (prj.getProjectManager().getId() == employee.getId())
			return true;
		else {
			for (Employee e : prj.getTeamMembers()) {
				if (e.getId() == employee.getId())
					return true;
			}
		}
		return false;
	}

	public boolean validateProject(Project prj) throws BadRequestException {

		if (prj.getEndDate() != null && prj.getStartDate() != null && prj.getEndDate().before(prj.getStartDate()))
			throw new BadRequestException("endDate cannot be before startDate");
		if (!clientDao.existsById(prj.getClient().getId()))
			throw new BadRequestException("client doesn't exist");
		if (!employeeDao.existsById(prj.getProjectManager().getId()))
			throw new BadRequestException("Project Manager doesn't exist");
		if (!employeeDao.findById(prj.getProjectManager().getId()).get().getRole().equals(EmployeeRoles.PRJ_MANAGER))
			throw new BadRequestException("ProjectManager role should be PRJ_MANAGER");

		return true;
	}
}
