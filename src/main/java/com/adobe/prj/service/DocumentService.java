package com.adobe.prj.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.adobe.prj.dao.DocumentDao;
import com.adobe.prj.dao.ProjectDao;
import com.adobe.prj.entity.Document;
import com.adobe.prj.entity.Employee;
import com.adobe.prj.exceptions.NotFoundException;

@Service
public class DocumentService {

	@Autowired
	private DocumentDao documentDao;
	@Autowired
	private ProjectDao projectDao;

	public Document findDocumentsByDocName(String fileName) {
		return documentDao.findByDocName(fileName);
	}

	public Document findDocumentsById(int id) throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final Employee employee = (Employee) auth.getPrincipal();

		Document d;
		try {
			d = documentDao.findById(id).get();
			return d;
		} catch (Exception e) {
			throw new NotFoundException("Document does not exist");
		}

		// if (d.getEmployee().getId() == employee.getId())
		// 	return d;

		// throw new AccessDeniedException("Employee dont have access to the dcument");
	}

	public Document addDocument(MultipartFile file) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final Employee employee = (Employee) auth.getPrincipal();

		Document doc = new Document();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		doc.setDocName(fileName);
		doc.setEmployee(employee);
		try {
			doc.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documentDao.save(doc);

	}

	public void deleteDocument(int id) throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final Employee employee = (Employee) auth.getPrincipal();

		Document d;
		try {
			d = documentDao.findById(id).get();
		} catch (Exception e) {
			throw new NotFoundException("Document does not exist");
		}

		if (d.getEmployee().getId() == employee.getId())
			documentDao.deleteById(id);
		else
			throw new AccessDeniedException("Employee dont have access to the document");
	}
}
