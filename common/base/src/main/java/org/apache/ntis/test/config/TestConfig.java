package org.apache.ntis.test.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.config.YamlPropertySourceLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class TestConfig implements InitializingBean {

	@Autowired
	private ConfigurableApplicationContext context;

	private final String propertylocation = "classpath:/server.yml";

	@Override
	public void afterPropertiesSet() throws Exception {
		YamlPropertySourceLoader yamlLoader = YamlPropertySourceLoader
				.matchAllLoader();
		ResourceLoader loader = new DefaultResourceLoader();

		context.getEnvironment()
				.getPropertySources()
				.addLast(
						yamlLoader.load("server",
								loader.getResource(propertylocation)));

	}

}
