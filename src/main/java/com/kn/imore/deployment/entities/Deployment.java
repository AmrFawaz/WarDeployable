package com.kn.imore.deployment.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.catalina.Server;

@Entity
public class Deployment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	// microservice (ms) information
	private String msName;
	private String msStatus;
	

	// deployment server information
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
    @JoinColumn(name = "serverId", nullable = false)
	private PhysicalServers server;
	
	
	public Deployment()
	{
		
	}
	public Deployment(String serverName, String serviceName, String status, String lastDeploymentDate, String size, String hostName, String serverPort, String username, String password,PhysicalServers server) {
		this.msName = serviceName;
		this.server = server;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMsName() {
		return msName;
	}

	public void setMsName(String msName) {
		this.msName = msName;
	}

	public String getMsStatus() {
		return msStatus;
	}

	public void setMsStatus(String msStatus) {
		this.msStatus = msStatus;
	}





	
	public PhysicalServers getServer() {
		return server;
	}
	public void setServer(PhysicalServers server) {
		this.server = server;
	}
}
