package com.adobe.prj.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.adobe.prj.entity.Employee;

public interface EmployeeDao extends JpaRepository<Employee, Integer> {

	List<Employee> findByEmail(String email);

	@Query(value = "select emp_id as id, email, first_name as firstName, last_name as lastName, role from employees", nativeQuery = true)
	List<Map<String, Object>> findAllInfo();
}
