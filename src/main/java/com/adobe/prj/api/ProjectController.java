package com.adobe.prj.api;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.adobe.prj.entity.Project;
import com.adobe.prj.exceptions.BadRequestException;
import com.adobe.prj.exceptions.NotFoundException;
import com.adobe.prj.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("api/projects")
@Api(value = "ProjectController", description = "REST APIs related to Project Entity!!!!")
public class ProjectController {
	@Autowired
	private ProjectService projectService;

	@ApiOperation(value = "add a project", response = Project.class,  tags = "addProject")
	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public @ResponseBody Project addProject(@Valid @RequestBody Project p) throws BadRequestException {
		return projectService.addProject(p);
	}

	@ApiOperation(value = "get a project by ID", response = Project.class, tags = "getProject")
	@GetMapping("/{prj_id}")
	public @ResponseBody Project getProject(@PathVariable("prj_id") int id) throws NotFoundException {
		return projectService.getProject(id);
	}

	@ApiOperation(value = "get all projects of an employee", response = Iterable.class, tags = "getEmpProjects")
	@GetMapping()
	public @ResponseBody List<Project> getEmpProjects(@RequestParam(name = "page", defaultValue = "-1") int page,
			@RequestParam(name = "size", defaultValue = "-1") int size) {
		return projectService.getProjectsByUser(page, size);
	}

	@ApiOperation(value = "delete project", tags = "deleteProject")
	@DeleteMapping("/{prj_id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteProject(@PathVariable("prj_id") int id) throws NotFoundException {
		projectService.deleteProject(id);
	}

	@ApiOperation(value = "delete list of projects", tags = "deleteProjects")
	@DeleteMapping()
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteProjects(@Valid @RequestBody List<Integer> ids) throws NotFoundException {
		projectService.deleteProjects(ids);
	}

	@ApiOperation(value = "update a project", response = Project.class, tags = "updateProject")
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Project updateProject(@Valid @RequestBody Map<String, Object> updates,
			@PathVariable("id") int id) throws NotFoundException, BadRequestException {
		return projectService.updateProject(updates, id);
	}

}
