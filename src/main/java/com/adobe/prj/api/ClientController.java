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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.prj.entity.Client;
import com.adobe.prj.exceptions.NotFoundException;
import com.adobe.prj.service.ClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("api/clients")
@Api(value = "ClientController", description = "REST APIs related to Client Entity!!!!")
public class ClientController {
	@Autowired
	private ClientService service;

	// http://localhost:8080/api/products GET
	// http://localhost:8080/api/products?page=0&size=4 GET

	@ApiOperation(value = "Get list of Clients in the System ", response = Iterable.class, tags = "getClients")
	@GetMapping()
	public @ResponseBody List<Client> getClients() {
		return service.getClients();

	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws NotFoundException
	 */
	@ApiOperation(value = "Get Client by ID ", response = Client.class, tags = "getClient")
	@GetMapping("/{pid}")
	public @ResponseBody Client getClient(@PathVariable("pid") int id) throws NotFoundException {
		return service.getClient(id);
	}

	@ApiOperation(value = "Add Client ", response = Client.class, tags = "addClient")
	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public @ResponseBody Client addClient(@RequestBody @Valid Client c) {
		System.out.println(c.toString());
		return service.addClient(c);
	}

	@ApiOperation(value = "Update a Client by ID ", response = Client.class, tags = "updateClient")
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Client updateClient(@RequestBody Map<String, Object> updates, @PathVariable("id") int id)
			throws NotFoundException {
		System.out.println(updates);
		Client c = service.getClient(id);
		return service.updateClient(updates, c);
	}

	@ApiOperation(value = "Delete Client by ID ", tags = "deleteClient")
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteClient(@PathVariable("id") int id) throws NotFoundException {
		service.deleteClient(id);
	}
}
