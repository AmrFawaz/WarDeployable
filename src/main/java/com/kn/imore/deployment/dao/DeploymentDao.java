package com.kn.imore.deployment.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kn.imore.deployment.entities.Deployment;
import com.kn.imore.deployment.entities.PhysicalServers;

public interface DeploymentDao extends CrudRepository<Deployment, Long> {
	
}
