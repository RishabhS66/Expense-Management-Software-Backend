package com.adobe.prj.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adobe.prj.entity.Client;

public interface ClientDao extends JpaRepository<Client, Integer> {

}
