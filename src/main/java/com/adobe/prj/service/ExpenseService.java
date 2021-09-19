package com.adobe.prj.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.adobe.prj.dao.DocumentDao;
import com.adobe.prj.dao.ExpenseDao;
import com.adobe.prj.entity.Document;
import com.adobe.prj.entity.Expense;
import com.adobe.prj.exceptions.BadRequestException;
import com.adobe.prj.exceptions.NotFoundException;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseDao expenseDao;
	
	@Autowired
	private DocumentDao documentDao;

	public Map<String, Double> getExpenseTotal(int pid) {
		HashMap<String, Double> map = new HashMap<>();
		double billable_amount = expenseDao.findBillableByProject(pid);
		double non_billable_amount = expenseDao.findNonBillableByProject(pid);
		map.put("Billable", billable_amount);
		map.put("Non-billable", non_billable_amount);
		map.put("Total", billable_amount + non_billable_amount);
		return map;
	}

	@Transactional
	public Expense addExpense(Expense e) {
		return expenseDao.save(e);
	}

	@Transactional
	public Expense getExpense(int id) throws NotFoundException {
		try {
			return expenseDao.findById(id).get();
		} catch (Exception e) {
			throw new NotFoundException("The expense does not exist");
		}
	}

	@Transactional
	public List<Expense> getExpenseByEmplyoeeId(int id) throws NotFoundException {
		try {
			return expenseDao.findByEmployee(id);
		} catch (Exception e) {
			throw new NotFoundException("The expense does not exist");
		}
	}

	@Transactional
	public List<Expense> getExpenseByProject(int id) throws NotFoundException {
		try {
			return expenseDao.findByProject(id);
		} catch (Exception e) {
			throw new NotFoundException("The expense does not exist");
		}
	}

	@Transactional
	public List<Expense> getExpenseByStatus(int id) throws NotFoundException {
		try {
			return expenseDao.findByStatus(id);
		} catch (Exception e) {
			throw new NotFoundException("Expense does not exist for the status");
		}
	}

	@Transactional
	public List<Expense> getAllExpense() {
		return expenseDao.findAll();
	}

	public void deleteExpense(int id) {
		expenseDao.deleteById(id);
	}

	public void deleteExpenses(List<Integer> expenseId) {
		expenseDao.deleteAllById(expenseId);
	}

	@Transactional
	public Page<Expense> paginateExpense(int page, int size) {
		return expenseDao.findAll(PageRequest.of(page, size));
	}

	@Transactional
	public List<Expense> getExpenseByStatusforProject(int pid, int status) {
		return expenseDao.findByProjectStatus(pid, status);
	}

	public Expense updateExpenseStatus(Map<String, Object> updates, Expense e) {
		PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(e);
		for (String k : updates.keySet()) {
			if (k == "status")
				myAccessor.setPropertyValue(k, updates.get(k));
		}
		return expenseDao.save(e);
	}

	@Valid
	@Transactional
	public Expense updateExpense(Map<String, Object> updates, Expense e) throws BadRequestException {
		PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(e);
		for (String k : updates.keySet()) {
			if (k == "endDate") {
				throw new BadRequestException("Date cannot be updated");

			} else if (k=="attachments") {
				@SuppressWarnings("unchecked")
				List<Map<String, Integer>> docs = (List<Map<String, Integer>>) updates.get(k);
				List<Integer> doc_ids = docs.stream().map(d -> d.get("id")).collect(Collectors.toList());
				Set<Document> doc_objs = doc_ids.stream().map(id -> documentDao.getById(id)).collect(Collectors.toSet());
				e.setAttachments(doc_objs);
			}
			else
				myAccessor.setPropertyValue(k, updates.get(k));
		}
		return expenseDao.save(e);
	}

//	public Page<Expense> paginateExpenseByProject(int page,int size,int id){
//	return expenseDao.findByProject(id)
//}

}
