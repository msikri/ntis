package org.apache.ntis.exp.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "org.apache.ntis.server.config" })
public class NtisServerStartup {

	private static Log logger = LogFactory.getLog(NtisServerStartup.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(NtisServerStartup.class, args);
	}

}
