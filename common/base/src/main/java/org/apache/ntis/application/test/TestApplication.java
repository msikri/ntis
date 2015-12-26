package org.apache.ntis.application.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = DispatcherServletAutoConfiguration.class)
@ComponentScan(basePackages = { "org.apache.ntis.application" })
public class TestApplication {

}
