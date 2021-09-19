package com.adobe.prj.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adobe.prj.entity.Expense;

public interface ExpenseDao extends JpaRepository<Expense, Integer> {

	@Query(value = "select * from expenses where status = :id", nativeQuery = true)
	List<Expense> findByStatus(@Param("id") int id);

	@Query("from Expense where employee_fk=:id")
	List<Expense> findByEmployee(@Param("id") int id);

	@Query("from Expense where project_fk=:id")
	List<Expense> findByProject(@Param("id") int id);

	@Query(value = "select * from expenses where project_fk = :id and status=:status", nativeQuery = true)
	List<Expense> findByProjectStatus(@Param("id") int id, @Param("status") int status);

	@Query(value = "select COALESCE(sum(amount),0) + COALESCE(sum(tax),0) from expenses where project_fk = :id and billable=true", nativeQuery = true)
	double findBillableByProject(@Param("id") int id);

	@Query(value = "select COALESCE(sum(amount),0) + COALESCE(sum(tax),0) from expenses where project_fk = :id and billable=false", nativeQuery = true)
	double findNonBillableByProject(@Param("id") int id);

}
