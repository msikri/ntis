package org.apache.ntis.exp.client.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.apache.ntis.exp.client",
		"com.service.inventory.client.config",
		"org.apache.ntis.ntisclient.config" })
public class NtisAwareInventoryClientConfig {

}
