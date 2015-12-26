package org.eclipse.ntis.server;

import org.apache.ntis.common.LoggerFactory;
import org.apache.ntis.logging.LogLevel;
import org.apache.ntis.ntisclient.NtisClient;
import org.apache.ntis.ntisclient.config.NtisClientConfig;
import org.apache.ntis.server.NtisServerStartup;
import org.apache.ntis.server.config.NtisServerConfig;
import org.apache.ntis.server.datamodel.GetProximityList;
import org.apache.ntis.server.datamodel.GetProximityListResponse;
import org.apache.ntis.server.datamodel.ServiceLocationType;
import org.apache.ntis.test.config.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Configuration
@ContextConfiguration(classes = { NtisServerConfig.class,
		NtisClientConfig.class, TestConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class NtisServerTest {

	@Autowired
	private NtisClient client;

	private ConfigurableApplicationContext context;

	@Before
	public void setUp() {
		context = SpringApplication.run(NtisServerStartup.class);
	}

	@After
	public void shutdown() {
		context.close();
	}

	int PORT = 9191;

	@Test
	public void runClient() throws Exception {

		GetProximityList list = new GetProximityList();
		list.setClientlocation("DataCenter1");
		ServiceLocationType type = new ServiceLocationType();
		type.getSite().add("DataCenter1");
		list.setServicelocation(type);

		GetProximityListResponse response = client.callService(list,
				"http://localhost:" + PORT + "/ntis");

		LoggerFactory.getInstance().getLogger()
				.message(LogLevel.INFO, response);

	}

}
