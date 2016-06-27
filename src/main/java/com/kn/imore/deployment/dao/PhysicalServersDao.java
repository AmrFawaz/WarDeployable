package com.kn.imore.deployment.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kn.imore.deployment.entities.PhysicalServers;

public interface PhysicalServersDao extends CrudRepository<PhysicalServers, Long> {
	
	
}
