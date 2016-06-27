package com.kn.imore.deployment.dao;

import org.springframework.data.repository.CrudRepository;

import com.kn.imore.deployment.entities.ServerTypes;

public interface ServerTypeDao extends CrudRepository<ServerTypes, Long>{
	
}
