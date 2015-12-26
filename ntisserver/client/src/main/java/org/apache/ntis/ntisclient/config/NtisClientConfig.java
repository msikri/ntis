package org.apache.ntis.ntisclient.config;

import org.apache.ntis.ntisclient.NtisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NtisClientConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public NtisClient client() {
		NtisClient client = new NtisClient();
		client.setRestTemplate(restTemplate());
		return client;
	}
}
