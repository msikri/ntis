package org.apache.ntis.server.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.apache.ntis.log.config",
		"org.apache.ntis.application.config", "org.apache.ntis.server" })
public class NtisServerConfig {

	@Bean
	public CommandLineRunner runner() {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				System.out.println("Started NTIS Server succcesfully....");
			}
		};
	}
}
