package com.adobe.prj.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adobe.prj.dao.ClientDao;
import com.adobe.prj.dao.ProjectDao;
import com.adobe.prj.entity.Client;
import com.adobe.prj.exceptions.NotFoundException;

@Service
public class ClientService {
	@Autowired
	private ClientDao clientDao;

	@Autowired
	private ProjectDao projectDao;

	public Client addClient(Client c) {
		return clientDao.save(c);
	}

	public List<Client> getClients() {
		return clientDao.findAll();
	}

	public void deleteClient(int id) throws NotFoundException {
		try {
			List<Integer> prjs = projectDao.findByClient(id);
			projectDao.deleteAllById(prjs);
			clientDao.deleteById(id);
		} catch (Exception e) {
			// System.out.println(e.getCause());
			throw new NotFoundException("Client with id " + id + " doesn't exist!!!");
		}
	}

	public Client getClient(int id) throws NotFoundException {
		try {
			return clientDao.findById(id).get();
		} catch (Exception e) {
			throw new NotFoundException("Client with id " + id + " doesn't exist!!!");
		}

	}

	@SuppressWarnings("unchecked")
	@Valid
	public Client updateClient(Map<String, Object> updates, Client c) {
		PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(c);
		// set the property directly, bypassing the mutator (if any)
		for (String k : updates.keySet()) {
			if (k == "address") {
				c.updateAddress((Map<String, Object>) updates.get(k));
			} else {
				myAccessor.setPropertyValue(k, updates.get(k));
			}
		}

		return clientDao.save(c);
	}

}
