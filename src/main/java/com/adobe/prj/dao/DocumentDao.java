package com.adobe.prj.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adobe.prj.entity.Document;

public interface DocumentDao extends JpaRepository<Document, Integer> {
	Document findByDocName(String fileName);
}
