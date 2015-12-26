package org.apache.ntis.inventory.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = DispatcherServletAutoConfiguration.class)
@ComponentScan(basePackages = { "org.apache.ntis.inventory.service.config" })
public class BootStartup {
	private static Log logger = LogFactory.getLog(BootStartup.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BootStartup.class, args);
	}	
}
