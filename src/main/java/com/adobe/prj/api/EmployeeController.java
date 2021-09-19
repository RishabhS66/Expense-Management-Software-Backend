package com.adobe.prj.api;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.prj.entity.Employee;
import com.adobe.prj.service.EmployeeService;

@CrossOrigin
@RestController
@RequestMapping("api/employees")
public class EmployeeController {
	@Autowired
	private EmployeeService service;
	


	@GetMapping()
	public @ResponseBody List<Map<String, Object>> getEmployees() {
		System.out.println(service.getEmployees());
		return service.getEmployees();

	}

	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public @ResponseBody Employee addEmployee(@RequestBody @Valid Employee e) {
		return service.addEmployee(e);
	}

//        
//        @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
//        public @ResponseBody Client updateClient(
//          @RequestBody Map<String, Object> updates,
//          @PathVariable("id") int id) throws NotFoundException{
//            System.out.println(updates);
//            Client c = service.getClient(id);
//            return service.updateClient(updates, c);
//        }
}
