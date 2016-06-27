package com.kn.imore.deployment.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.cargo.container.deployable.Deployable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.cargo.container.deployable.DeployableType;
import org.codehaus.cargo.container.deployer.Deployer;
import org.codehaus.cargo.container.property.GeneralPropertySet;
import org.codehaus.cargo.container.property.RemotePropertySet;
import org.codehaus.cargo.container.property.ServletPropertySet;
import org.codehaus.cargo.container.tomcat.Tomcat8xRemoteContainer;
import org.codehaus.cargo.container.tomcat.Tomcat8xRemoteDeployer;
import org.codehaus.cargo.container.tomcat.TomcatRuntimeConfiguration;
import org.codehaus.cargo.generic.deployable.DefaultDeployableFactory;
import org.codehaus.cargo.generic.deployer.DefaultDeployerFactory;
import org.codehaus.cargo.generic.deployer.DeployerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kn.imore.deployment.dao.DeploymentDao;
import com.kn.imore.deployment.dao.PhysicalServersDao;
import com.kn.imore.deployment.entities.Deployment;
import com.kn.imore.deployment.entities.PhysicalServers;
import com.kn.imore.deployment.models.DeploymentModel;
import com.kn.imore.deployment.models.PhysicalServerModel;

import javassist.bytecode.Mnemonic;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

@Controller
public class DeploymentController {

	@Autowired
	private PhysicalServersDao physicalServersDao;

	@Autowired
	private DeploymentDao deploymentDao;

	private static final Logger log = LoggerFactory.getLogger(DeploymentController.class);

	///////List All Uploaded MSs ///////////////////////////////
	@RequestMapping(method = RequestMethod.GET, value = "/getAllMicroService")
	public String getAllMicroServices(Model model, RedirectAttributes redirectAttributes) {
		File currentDirFile = new File("DeployedServices");
		if (!currentDirFile.exists()) {
			redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
			return "vied";
		}
		String helper = currentDirFile.getAbsolutePath();

		File rootFolder = new File(helper + "\\");
		List<String> fileNames = Arrays.stream(rootFolder.listFiles()).map(f -> f.getName())
				.collect(Collectors.toList());

		model.addAttribute("files",
				Arrays.stream(rootFolder.listFiles()).sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
						.map(f -> f.getName()).collect(Collectors.toList()));

		ArrayList<PhysicalServerModel> allServers = getPhysicalServers();
		model.addAttribute("allServers", allServers);
		return "view";
	}

	

	
	////////// List All Added Servers With Thier MSs/////////////////////////////////
	@RequestMapping(value = "/getPhysicalServers", method = RequestMethod.GET)
	public @ResponseBody ArrayList<PhysicalServerModel> getPhysicalServers() {
		ArrayList<PhysicalServerModel> allPhysicalServers = new ArrayList<PhysicalServerModel>();
		for (PhysicalServers server : physicalServersDao.findAll()) {
			PhysicalServerModel serverModel = new PhysicalServerModel();
			serverModel.setId(server.getId());
			serverModel.setServerName(server.getServerName());
			serverModel.setHostName(server.getHostName());
			serverModel.setUsername(server.getUsername());
			serverModel.setPassword(server.getPassword());
			serverModel.setPort(server.getPort());
			serverModel.setType(server.getType());
			serverModel.setMicroServices(server.getMicroServices());
			allPhysicalServers.add(serverModel);

		}
		return allPhysicalServers;
	}
	
	
	
	// ////////List All Deployed MSs/////////////////////////
	@RequestMapping(value = "/getDeploymentServices", method = RequestMethod.POST)
	public ArrayList<DeploymentModel> getDeploymentServices() {
		ArrayList<DeploymentModel> allDeployment = new ArrayList<DeploymentModel>();
		for (Deployment deployment : deploymentDao.findAll()) {
			DeploymentModel deploymentModel = new DeploymentModel();
			deploymentModel.setId(deployment.getId());
			deploymentModel.setServerHostName(deployment.getServer().getHostName());
			deploymentModel.setServerPort(deployment.getServer().getPort());
			deploymentModel.setServerUserame(deployment.getServer().getUsername());
			deploymentModel.setServerPassword(deployment.getServer().getPassword());
			deploymentModel.setServerName(deployment.getServer().getServerName());
			deploymentModel.setMsName(deployment.getMsName());
			deploymentModel.setMsStatus(deployment.getMsStatus());
			allDeployment.add(deploymentModel);

		}
		return allDeployment;
	}
	
	
	////// Upload MS to Root Folder///////////////////////////////
	@RequestMapping(value = "/uploadMicroService", method = RequestMethod.POST)
	public String uploadMicroService(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		String fileName = file.getOriginalFilename();
		if (file.getOriginalFilename().contains("/")) {
			redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
			return "view";
		}
		if (file.getOriginalFilename().contains("/")) {
			redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
			return "view";
		}

		if (!file.isEmpty()) {
			try {
				File currentDirFile = new File("DeployedServices");
				if (!currentDirFile.exists()) {
					currentDirFile.mkdir();
				}
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
				Date date = new Date();

				String rootProject = currentDirFile.getAbsolutePath();
				int lastPeriodPos = file.getOriginalFilename().lastIndexOf('.');
				if (lastPeriodPos > 0)
					fileName = fileName.substring(0, lastPeriodPos);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(
						new File(rootProject + "\\" + fileName + dateFormat.format(date) + ".war")));
				FileCopyUtils.copy(file.getInputStream(), stream);
				stream.close();
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message",
						"You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("message",
					"You failed to upload " + file.getOriginalFilename() + " because the file was empty");
		}
		return "view";
	}

	
	////////  Deploy Micro Service in Multi Servers
	@RequestMapping(value = { "/deployMS" })
	public String deployMS(@RequestParam("fileName") String fileName, @RequestParam("servers") List<String> servers,
			Model model) {

		for (String serverId : servers) {
			PhysicalServers server = physicalServersDao.findOne(Long.parseLong(serverId));
			deployMS(fileName,server);
		}
		ArrayList<PhysicalServerModel> allServers = getPhysicalServers();
		ArrayList<DeploymentModel> deployedServices = getDeploymentServices();
		model.addAttribute("allServers", allServers);
		model.addAttribute("deployedServices", deployedServices);
		return "view";
	}
	
	
	////////  Undeploy Micro Service in Multi Servers
	@RequestMapping(value = { "/undeployMS" }, method = RequestMethod.POST)
	public String undeployMS(@RequestParam("msId") String msId, @RequestParam("msName") String fileName, @RequestParam("serverId") List<String> servers,
			Model model) {

		for (String serverId : servers) {
			PhysicalServers server = physicalServersDao.findOne(Long.parseLong(serverId));
			undeployMS(msId,fileName,server);
		}
		ArrayList<PhysicalServerModel> allServers = getPhysicalServers();
		ArrayList<DeploymentModel> deployedServices = getDeploymentServices();
		model.addAttribute("allServers", allServers);
		model.addAttribute("deployedServices", deployedServices);
		return "view";
	}
	
	
	
	///////// Start MSs On Server/////////////////////////////
	@RequestMapping(value = { "/startMS" }, method = RequestMethod.POST)
	public String startMS(@RequestParam("msId") List<String> msId, Model model) {

		startMS(msId);
		ArrayList<PhysicalServerModel> allServers = getPhysicalServers();
		ArrayList<DeploymentModel> deployedServices = getDeploymentServices();
		model.addAttribute("allServers", allServers);
		model.addAttribute("deployedServices", deployedServices);
		return "view";
	}

	
	
	////////////// Stop MSs on Server///////////////////
	@RequestMapping(value = { "/stopMS" }, method = RequestMethod.POST)
	public String stopMS(@RequestParam("msId") List<String> msId, Model model) {

		stopMS(msId);
		ArrayList<PhysicalServerModel> allServers = getPhysicalServers();
		ArrayList<DeploymentModel> deployedServices = getDeploymentServices();
		model.addAttribute("allServers", allServers);
		model.addAttribute("deployedServices", deployedServices);
		return "view";
	}
	
	
	
	
	//////////////// Dublicate MS To Other Servers/////////////////////////////
	@RequestMapping(value = { "/duplicateMS" }, method = RequestMethod.POST)
	public String duplicateMS(@RequestParam("msId") String msId,@RequestParam("msName") String msName, @RequestParam("servers") List<String> servers,@RequestParam("currentServerId") String currentServerId, @RequestParam("removeSource") boolean removeSource, Model model ) {
		
		Deployment microServic = deploymentDao.findOne(Long.parseLong(msId));
		PhysicalServers server = physicalServersDao.findOne(Long.parseLong(currentServerId));
		if(removeSource){
			undeployMS(msId, microServic.getMsName(), server);
		}
		
		
		for (String serverId : servers) {
			PhysicalServers serverEntity = physicalServersDao.findOne(Long.parseLong(serverId));
			deployMS(msName,serverEntity);
		}
		ArrayList<PhysicalServerModel> allServers = getPhysicalServers();
		ArrayList<DeploymentModel> deployedServices = getDeploymentServices();
		model.addAttribute("allServers", allServers);
		model.addAttribute("deployedServices", deployedServices);
		
		
		
		return "view";
	}
	
	
	

	@RequestMapping(value = { "/goToDuplicateMS" }, method = RequestMethod.POST)
	public String routToduplicateMS(@RequestParam("msId") String msId, @RequestParam("serverId") String currentServer, Model model ) {

		Deployment ms = deploymentDao.findOne(Long.parseLong(msId));
		
		
		ArrayList<PhysicalServerModel> allServers = getPhysicalServers();
		model.addAttribute("allServers", allServers);
		model.addAttribute("microService", ms);
		model.addAttribute("currentServer",currentServer);
		return "duplicate";
	}
	
	

	public void deployMS(String msName, PhysicalServers server) {
		File currentDirFile = new File("DeployedServices");
		if (!currentDirFile.exists()) {
			currentDirFile.mkdir();
		}
		String rootProject = currentDirFile.getAbsolutePath();
		DefaultDeployableFactory factory = new DefaultDeployableFactory();
		Deployable deplyableReference = factory.createDeployable(server.getType(), rootProject + "\\" + msName,
				DeployableType.WAR);

		TomcatRuntimeConfiguration tomcatApplicationServerConifugration = new TomcatRuntimeConfiguration();
		tomcatApplicationServerConifugration.setProperty(GeneralPropertySet.HOSTNAME, server.getHostName());
		tomcatApplicationServerConifugration.setProperty(ServletPropertySet.PORT, server.getPort());
		tomcatApplicationServerConifugration.setProperty(RemotePropertySet.USERNAME, server.getUsername());
		tomcatApplicationServerConifugration.setProperty(RemotePropertySet.PASSWORD, server.getPassword());

		Deployer applicationServerDeployer = new Tomcat8xRemoteDeployer(
				new Tomcat8xRemoteContainer(tomcatApplicationServerConifugration));
		applicationServerDeployer.deploy(deplyableReference);

	
		Deployment deployable = new Deployment();
		deployable.setMsName(msName);
		deployable.setServer(server);
		deployable.setMsStatus("Running");
		deploymentDao.save(deployable);
	

	}

	public void undeployMS(String msId,String msName, PhysicalServers server) {
		File currentDirFile = new File("DeployedServices");
		if (!currentDirFile.exists()) {
			currentDirFile.mkdir();
		}
		String rootProject = currentDirFile.getAbsolutePath();
		DefaultDeployableFactory factory = new DefaultDeployableFactory();
		Deployable deplyableReference = factory.createDeployable(server.getType(), rootProject + "\\" + msName,
				DeployableType.WAR);

		TomcatRuntimeConfiguration tomcatApplicationServerConifugration = new TomcatRuntimeConfiguration();
		tomcatApplicationServerConifugration.setProperty(GeneralPropertySet.HOSTNAME, server.getHostName());
		tomcatApplicationServerConifugration.setProperty(ServletPropertySet.PORT, server.getPort());
		tomcatApplicationServerConifugration.setProperty(RemotePropertySet.USERNAME, server.getUsername());
		tomcatApplicationServerConifugration.setProperty(RemotePropertySet.PASSWORD, server.getPassword());

		Deployer applicationServerDeployer = new Tomcat8xRemoteDeployer(
				new Tomcat8xRemoteContainer(tomcatApplicationServerConifugration));
		applicationServerDeployer.undeploy(deplyableReference);



		Deployment deployable = deploymentDao.findOne(Long.parseLong(msId));

		deploymentDao.delete(deployable);

	}
	
	public void startAllMSInServer(String msId,String msName, PhysicalServers server) {
		File currentDirFile = new File("DeployedServices");
		
		for(Deployment ms : server.getMicroServices()){
			
			if (!currentDirFile.exists()) {
				currentDirFile.mkdir();
			}
			String rootProject = currentDirFile.getAbsolutePath();
			DefaultDeployableFactory factory = new DefaultDeployableFactory();
			Deployable deplyableReference = factory.createDeployable(server.getType(), rootProject + "\\" + ms.getMsName(),
					DeployableType.WAR);
			
			TomcatRuntimeConfiguration tomcatApplicationServerConifugration = new TomcatRuntimeConfiguration();
			tomcatApplicationServerConifugration.setProperty(GeneralPropertySet.HOSTNAME, server.getHostName());
			tomcatApplicationServerConifugration.setProperty(ServletPropertySet.PORT, server.getPort());
			tomcatApplicationServerConifugration.setProperty(RemotePropertySet.USERNAME, server.getUsername());
			tomcatApplicationServerConifugration.setProperty(RemotePropertySet.PASSWORD, server.getPassword());

			Deployer applicationServerDeployer = new Tomcat8xRemoteDeployer(
					new Tomcat8xRemoteContainer(tomcatApplicationServerConifugration));
			applicationServerDeployer.start(deplyableReference);
			
			Deployment deployable = deploymentDao.findOne(ms.getId());
			deployable.setMsStatus("Running");
			deploymentDao.save(deployable);
		}
	}
	
	
	public void startMS(List<String> msIds) {
		
		for(String microServiceId :  msIds){
			Deployment ms = deploymentDao.findOne(Long.parseLong(microServiceId));
			PhysicalServers server = ms.getServer();
			
			
			File currentDirFile = new File("DeployedServices");
			if (!currentDirFile.exists()) {
				currentDirFile.mkdir();
			}
			String rootProject = currentDirFile.getAbsolutePath();
			DefaultDeployableFactory factory = new DefaultDeployableFactory();
			Deployable deplyableReference = factory.createDeployable(server.getType(), rootProject + "\\" + ms.getMsName(), DeployableType.WAR);
			
			TomcatRuntimeConfiguration tomcatApplicationServerConifugration = new TomcatRuntimeConfiguration();
			tomcatApplicationServerConifugration.setProperty(GeneralPropertySet.HOSTNAME, server.getHostName());
			tomcatApplicationServerConifugration.setProperty(ServletPropertySet.PORT, server.getPort());
			tomcatApplicationServerConifugration.setProperty(RemotePropertySet.USERNAME, server.getUsername());
			tomcatApplicationServerConifugration.setProperty(RemotePropertySet.PASSWORD, server.getPassword());
	
			Deployer applicationServerDeployer = new Tomcat8xRemoteDeployer(
					new Tomcat8xRemoteContainer(tomcatApplicationServerConifugration));
			applicationServerDeployer.start(deplyableReference);
			
			Deployment deployable = deploymentDao.findOne(ms.getId());
			deployable.setMsStatus("Running");
			deploymentDao.save(deployable);
		}
	}
	
	public void stopMS(List<String> msIds) {
			
		for(String microServiceId :  msIds){
			Deployment ms = deploymentDao.findOne(Long.parseLong(microServiceId));
			PhysicalServers server = ms.getServer();
			
			
			File currentDirFile = new File("DeployedServices");
			if (!currentDirFile.exists()) {
				currentDirFile.mkdir();
			}
			String rootProject = currentDirFile.getAbsolutePath();
			DefaultDeployableFactory factory = new DefaultDeployableFactory();
			Deployable deplyableReference = factory.createDeployable(server.getType(), rootProject + "\\" + ms.getMsName(), DeployableType.WAR);
			
			TomcatRuntimeConfiguration tomcatApplicationServerConifugration = new TomcatRuntimeConfiguration();
			tomcatApplicationServerConifugration.setProperty(GeneralPropertySet.HOSTNAME, server.getHostName());
			tomcatApplicationServerConifugration.setProperty(ServletPropertySet.PORT, server.getPort());
			tomcatApplicationServerConifugration.setProperty(RemotePropertySet.USERNAME, server.getUsername());
			tomcatApplicationServerConifugration.setProperty(RemotePropertySet.PASSWORD, server.getPassword());
	
			Deployer applicationServerDeployer = new Tomcat8xRemoteDeployer(
					new Tomcat8xRemoteContainer(tomcatApplicationServerConifugration));
			applicationServerDeployer.stop(deplyableReference);
			
			Deployment deployable = deploymentDao.findOne(ms.getId());
			deployable.setMsStatus("Stopped");
			deploymentDao.save(deployable);
		}
	}
	
	
	

	
	

	


	//////////////////////////////////////////// Landing //////////////////////////////////////
	@RequestMapping(value = { "/landing" })
	public String landing(HttpSession session, Model model, HttpServletRequest httpRequest) {
		ArrayList<PhysicalServerModel> allServers = getPhysicalServers();
		ArrayList<DeploymentModel> deployedServices = getDeploymentServices();
   		model.addAttribute("allServers", allServers);
		model.addAttribute("deployedServices", deployedServices);
		return "view";
	}

}
