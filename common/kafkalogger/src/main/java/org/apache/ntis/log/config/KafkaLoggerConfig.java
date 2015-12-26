package org.apache.ntis.log.config;

import org.apache.ntis.log.kafka.KafkaLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@ComponentScan(basePackages = "org.apache.ntis.log.time")
public class KafkaLoggerConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${kafka.brokerlist}")
	private String brokerlist;

	@Bean
	public KafkaLogger kafkaconfig() {
		KafkaLogger logger = new KafkaLogger();
		logger.setName(applicationName);
		logger.setBrokerlist(brokerlist);
		return logger;
	}
}
