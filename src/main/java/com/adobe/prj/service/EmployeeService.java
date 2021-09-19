package com.adobe.prj.service;

import java.util.List;
import java.util.Map;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import com.adobe.prj.dao.EmployeeDao;
import com.adobe.prj.entity.Employee;
import com.adobe.prj.exceptions.NotFoundException;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeDao employeeDao;
	
	private BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

	public Employee addEmployee(Employee e) {
//		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		e.setPassword(passwordEncryptor.encryptPassword(e.getPassword()));
		return employeeDao.save(e);
	}

	@Transactional
	public Employee getEmployeeByEmail(String email) throws NotFoundException {
		try {
			return employeeDao.findByEmail(email).get(0);
		} catch (Exception e) {
			throw new NotFoundException("Employee with email " + email + " doesn't exist!!!");
		}

	}

	public void deleteEmployee(int id) {
		employeeDao.deleteById(id);
	}

	public Employee updatePassword(int id, String password) throws NotFoundException {
		Employee e = getEmployee(id);
		e.setPassword(passwordEncryptor.encryptPassword(password));
		e.setIsPasswordTemp(false);
		return employeeDao.save(e);
	}

	public List<Map<String, Object>> getEmployees() {

		return employeeDao.findAllInfo();
	}

	public Employee getEmployee(int id) throws NotFoundException {
		try {
			return employeeDao.findById(id).get();
		} catch (Exception e) {
			throw new NotFoundException("Employee with id " + id + " doesn't exist!!!");
		}

	}
}
