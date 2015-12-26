package org.apache.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.xml.xsd.SimpleXsdSchema;

@Configuration
@ComponentScan(basePackages = { "org.apache.ntis.application.config",
		"org.apache.ntis.log.config" })
public class InterfaceConfig{


	@Bean
	public SimpleXsdSchema inventoryschema() {
		return new SimpleXsdSchema(new ClassPathResource(
				"/META-INF/Inventory.xsd"));
	}

}
