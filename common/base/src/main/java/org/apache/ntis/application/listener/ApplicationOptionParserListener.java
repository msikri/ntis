package org.apache.ntis.application.listener;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.springframework.boot.SpringApplicationEnvironmentAvailableEvent;
import org.springframework.boot.config.YamlPropertySourceLoader;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.JOptCommandLinePropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

public class ApplicationOptionParserListener implements
		ApplicationListener<SpringApplicationEnvironmentAvailableEvent> {

	private final static String CONFIG_OPTIONS = "config";

	private final String propertylocation = "classpath:/server.yml";

	public ApplicationOptionParserListener() {
		System.out.println("Starting listner");
	}

	private JOptCommandLinePropertySource getCommandFileName(String... args) {
		OptionParser parser = new OptionParser();
		parser.accepts(CONFIG_OPTIONS).withRequiredArg();
		OptionSet options = parser.parse(args);
		JOptCommandLinePropertySource source = new JOptCommandLinePropertySource(
				options);
		return source;
	}

	private void initializeApplicationProperties(
			SpringApplicationEnvironmentAvailableEvent event) {
		String args[] = event.getArgs();
		ConfigurableEnvironment env = event.getEnvironment();
		System.out.println("====> Reading command line arguments "+args);
		JOptCommandLinePropertySource source = getCommandFileName(args);
		env.getPropertySources().addFirst(source);
		OptionSet option = source.getSource();

		String location = propertylocation;

		if (option.has(CONFIG_OPTIONS)) {
			location = "classpath:/" + option.valueOf(CONFIG_OPTIONS);
		}

		YamlPropertySourceLoader yamlLoader = YamlPropertySourceLoader
				.matchAllLoader();
		ResourceLoader loader = new DefaultResourceLoader();

		env.getPropertySources().addLast(
				yamlLoader.load("server", loader.getResource(location)));

	}

	// @Override
	// public int getOrder() {
	// return Ordered.HIGHEST_PRECEDENCE;
	// }

	@Override
	public void onApplicationEvent(
			SpringApplicationEnvironmentAvailableEvent event) {
		initializeApplicationProperties(event);

	}

}
