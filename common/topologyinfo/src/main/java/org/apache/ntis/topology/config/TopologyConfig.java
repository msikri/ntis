package org.apache.ntis.topology.config;

import java.util.Map;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.SimpleThreadScope;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

@Configuration
@ComponentScan(basePackages = { "org.apache.ntis.topology.info" })
public class TopologyConfig {

	@Bean
	public CustomScopeConfigurer configureThreadScope() {
		CustomScopeConfigurer configure = new CustomScopeConfigurer();
		Map<String, Object> m = Maps.newHashMap(ImmutableMap
				.<String, Object> builder()
				.put("threadscope", new SimpleThreadScope()).build());
		configure.setScopes(m);
		return configure;
	}
}
