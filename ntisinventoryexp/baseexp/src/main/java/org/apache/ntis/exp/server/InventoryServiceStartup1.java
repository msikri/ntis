package org.apache.ntis.exp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = DispatcherServletAutoConfiguration.class)
@ComponentScan(basePackages = { "org.apache.ntis.inventory.service.config" })
public class InventoryServiceStartup1 {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(InventoryServiceStartup1.class, args);
	}	
}
