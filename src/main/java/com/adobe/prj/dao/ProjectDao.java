package com.adobe.prj.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adobe.prj.entity.Project;

public interface ProjectDao extends JpaRepository<Project, Integer> {

	@Query(value = "Select * from projects where project_manager_fk = :emp_id UNION"
			+ "(SELECT * FROM projects WHERE prj_id IN"
			+ "(SELECT prj_id FROM employee_projects where emp_id= :emp_id))", nativeQuery = true)
	List<Project> findByUser(@Param("emp_id") int emp_id);

	@Query(value = "Select prj_id from projects where client_fk = :client_id ", nativeQuery = true)
	List<Integer> findByClient(@Param("client_id") int client_id);
}
