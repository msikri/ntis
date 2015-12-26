package org.apache.ntis.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ConfigurationProperties
public class Application implements CommandLineRunner {

	@Value("${spring.application.name}")
	private String application;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("============================");
		System.out.println("       STARTING " + application);
		System.out.println("============================");
	}

	public String getApplication() {
		return application;
	}

}
