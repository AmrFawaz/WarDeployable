package com.kn.imore.deployment;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.kn.imore.deployment.controllers.DeploymentController;

@SpringBootApplication
@ConfigurationProperties("oracle")
public class CargoWebDemo2Application {

	public static String ROOT = "upload-dir";
	
	public static void main(String[] args) {
		SpringApplication.run(CargoWebDemo2Application.class, args);
	}

	
	@Bean
    CommandLineRunner init() {
        return (String[] args) -> {
            new File(ROOT).mkdir();
        };
    }

}
