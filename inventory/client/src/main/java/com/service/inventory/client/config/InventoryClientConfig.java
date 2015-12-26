package com.service.inventory.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
// @ComponentScan(basePackages = { "org.apache.ntis.topology.config",
// "org.apache.ntis.log.config" })
@ComponentScan(basePackages = { "org.apache.ntis.topology.config",
		"com.service.inventory.client", "org.apache.inventory.config",
		"org.apache.ntis.log.config" })
public class InventoryClientConfig {

	@Bean
	public WebServiceTemplate webServiceTemplate() {
		return new WebServiceTemplate();
	}

}
