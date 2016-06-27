package com.kn.imore.deployment.entities;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.tools.ant.taskdefs.optional.depend.Depend;

@Entity
public class PhysicalServers {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String serverName;
	private String hostName;
	private String port;
	private String username;
	private String password;
	private String type;
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToMany(mappedBy = "server")
	private List<Deployment> microServices;
	


	public PhysicalServers()
	{
		
	}
	
	public PhysicalServers(String hostName, String port, String userName, String password) {
		this.hostName = hostName;
		this.port = port;
		this.username = userName;
		this.password = password;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	
	public List<Deployment> getMicroServices() {
		return microServices;
	}

	public void setMicroServices(List<Deployment> microServices) {
		this.microServices = microServices;
	}


}
