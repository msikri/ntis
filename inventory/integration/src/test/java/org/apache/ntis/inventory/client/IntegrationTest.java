package org.apache.ntis.inventory.client;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;

import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.inventory.model.InventoryStatusType;
import org.apache.inventory.model.InventoryType;
import org.apache.inventory.model.ObjectFactory;
import org.apache.ntis.inventory.service.BootStartup;
import org.apache.ntis.test.config.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.service.inventory.client.InventoryClient;
import com.service.inventory.client.config.InventoryClientConfig;
import com.service.inventory.dao.IDataService;
import com.service.inventory.dao.Product;
import com.service.inventory.dao.config.DaoConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InventoryClientConfig.class, DaoConfig.class,
		TestConfig.class })
public class IntegrationTest {

	@Autowired
	private InventoryClient webservice;

	@Autowired
	private IDataService manager;

	@Value("${spring.inventoryservice.name}")
	private String serviceName;

	private ConfigurableApplicationContext context;

	@Before
	public void setUp() {
		context = SpringApplication.run(BootStartup.class);
	}

	@After
	public void shutdown() {
		context.close();
	}

	@Test
	public void getHelloService() throws Exception {

		Product product1 = new Product();
		product1.setSkuid(1L);
		product1.setName("Product1");
		product1.setDescription("Des for Product");
		product1.setStatus(InventoryStatusType.IN_STOCK);
		product1.setPrice(1);

		manager.saveProduct(product1);

		ObjectFactory factory = new ObjectFactory();
		InventoryIds ids = factory.createInventoryIds();
		ids.getSkuids().add(product1.getSkuid());
		JAXBContext context = JAXBContext.newInstance(InventoryIds.class,
				InventoryList.class, InventoryType.class,
				InventoryStatusType.class);

		StringWriter writer = new StringWriter();
		context.createMarshaller().marshal(ids, writer);
		String xmlString = writer.toString();
		System.err.println(xmlString);

		InventoryList responselist = factory.createInventoryList();
		InventoryType invent = factory.createInventoryType();
		invent.setName(product1.getName());
		invent.setPrice(product1.getPrice());
		invent.setStatus(product1.getStatus());
		responselist.getInventory().add(invent);

		webservice.setAddress("http://localhost:9191/inventory/service",
				serviceName);
		// Thread.currentThread().sleep(1000 * 5);
		InventoryList actualresponse = webservice.getInventory(ids);

		System.out.println("Response=" + actualresponse);

		// Assert.assertNotNull(actualresponse);
		// Assert.assertEquals(actualresponse.getInventory().size(), 1);
		// Assert.assertEquals(actualresponse.getInventory().get(0).getName(),
		// product1.getName());
		// Assert.assertEquals(actualresponse.getInventory().get(0).getPrice(),
		// product1.getPrice());
		// Assert.assertEquals(actualresponse.getInventory().get(0).getStatus(),
		// product1.getStatus());

	}

}
