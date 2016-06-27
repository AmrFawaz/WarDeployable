package com.kn.imore.deployment.models;

import java.util.List;

import com.kn.imore.deployment.entities.Deployment;

public class PhysicalServerModel {
	private long id;
	private String serverName;
	private String hostName;
	private String port;
	private String username;
	private String password;
	private String type;
	private List<Deployment> microServices;
	
	
	public List<Deployment> getMicroServices() {
		return microServices;
	}
	public void setMicroServices(List<Deployment> microServices) {
		this.microServices = microServices;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	
	
}
