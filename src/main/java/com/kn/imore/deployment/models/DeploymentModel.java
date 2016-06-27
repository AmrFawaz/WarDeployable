package com.kn.imore.deployment.models;

import java.util.List;

public class DeploymentModel {
	
	private long id;
	private String serverHostName;
	private String serverPort;
	private String serverUserame;
	private String serverPassword;
	private String serverName;
	private String msName;
	private String msStatus;
	private String lastDeploymentDate;
	private String msSize;
	
	public String getServerHostName() {
		return serverHostName;
	}
	public void setServerHostName(String serverHostName) {
		this.serverHostName = serverHostName;
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
	public String getMsSize() {
		return msSize;
	}
	public void setMsSize(String msSize) {
		this.msSize = msSize;
	}




	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getServerUserame() {
		return serverUserame;
	}
	public void setServerUserame(String serverUserame) {
		this.serverUserame = serverUserame;
	}
	public String getServerPassword() {
		return serverPassword;
	}
	public void setServerPassword(String password) {
		this.serverPassword = password;
	}

	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getLastDeploymentDate() {
		return lastDeploymentDate;
	}
	public void setLastDeploymentDate(String lastDeploymentDate) {
		this.lastDeploymentDate = lastDeploymentDate;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

}
