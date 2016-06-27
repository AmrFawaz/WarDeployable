package com.kn.imore.deployment.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kn.imore.deployment.dao.DeploymentDao;
import com.kn.imore.deployment.dao.PhysicalServersDao;
import com.kn.imore.deployment.dao.ServerTypeDao;
import com.kn.imore.deployment.entities.PhysicalServers;
import com.kn.imore.deployment.entities.ServerTypes;
import com.kn.imore.deployment.models.DeploymentModel;
import com.kn.imore.deployment.models.PhysicalServerModel;
import com.kn.imore.deployment.models.ServerTypeModel;

@Controller
@RequestMapping(value = "/servers")
public class ServerController {

	
	@Autowired
	private PhysicalServersDao physicalServersDao;

	@Autowired
	private ServerTypeDao serverTypesDao;

	//////////// List All Server Types
	public List<ServerTypeModel> getServerTypes() {
		ArrayList<ServerTypeModel> serverModels = new ArrayList<ServerTypeModel>();
		ServerTypeModel serverTypeModel;
		for (ServerTypes server : serverTypesDao.findAll()) {
			serverTypeModel = new ServerTypeModel();
			serverTypeModel.setId(server.getId());
			serverTypeModel.setName(server.getName());
			serverTypeModel.setCode(server.getCode());
			serverTypeModel.setVersion(server.getVersion());
			serverModels.add(serverTypeModel);
		}
		return serverModels;
	}

	
	//////////////// Rout To Add Server//////////////
	@RequestMapping(value = { "/addNewServer" }, method = RequestMethod.POST)
	public String addNew(Model model) {
		model.addAttribute("serverTypes", getServerTypes());
		return "addServerTemplate";
	}
	
	
	//////// Add New Server//////////////////
	@RequestMapping(value = {"/addServer"}, method = RequestMethod.POST)
	public String addNewServer(@RequestParam("serverName") String serverName, @RequestParam("hostName") String hostName, @RequestParam("port") String port, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("serverType") String serverType){
		PhysicalServers server = new PhysicalServers();
		server.setServerName(serverName);
		server.setHostName(hostName);
		server.setPort(port);
		server.setUsername(username);
		server.setPassword(password);
		server.setType(serverType);
		physicalServersDao.save(server);		
		return "view";
	}
	
	
	
	
	//////// Rout To Manage Server ///////////////
	@RequestMapping(value = { "/manageServer" }, method = RequestMethod.POST)
	public String manageServer(@RequestParam("id") String id, Model model) {
		PhysicalServers server = physicalServersDao.findOne(Long.parseLong(id));
		PhysicalServerModel serverModel = new PhysicalServerModel();
		serverModel.setServerName(server.getServerName());
		serverModel.setHostName(server.getHostName());
		serverModel.setType(server.getType());
		serverModel.setId(server.getId());
		serverModel.setPassword(server.getPassword());
		serverModel.setUsername(server.getUsername());
		serverModel.setPort(server.getPort());
		model.addAttribute("serverTypes", getServerTypes());
		model.addAttribute("server",serverModel);
		
		return "manageServer";
	}
	
	
	///////// Manage Server/////////////////
	@RequestMapping(value = {"/editServer"}, method = RequestMethod.POST)
	public String manageServer(@RequestParam("id") String id, @RequestParam("serverName") String serverName, @RequestParam("hostName") String hostName, @RequestParam("port") String port, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("serverType") String serverType){
		PhysicalServers server = physicalServersDao.findOne(Long.parseLong(id));
		server.setServerName(serverName);
		server.setHostName(hostName);
		server.setPort(port);
		server.setUsername(username);
		server.setPassword(password);
		server.setType(serverType);
		physicalServersDao.save(server);		
		return "view";
	}
	
	
	////////// Delete Server////////////
	@RequestMapping(value = { "/deleteServer" }, method = RequestMethod.POST)
	public String deleteServer(@RequestParam("id") String id){
		PhysicalServers server = physicalServersDao.findOne(Long.parseLong(id));
		physicalServersDao.delete(server);		
		return "view";
	}
	


}
