package org.apache.ntis.exp;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;

import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.inventory.model.InventoryStatusType;
import org.apache.inventory.model.InventoryType;
import org.apache.inventory.model.ObjectFactory;
import org.apache.ntis.exp.client.NtisAwareInventoryClient;
import org.apache.ntis.exp.client.config.NtisAwareInventoryClientConfig;
import org.apache.ntis.exp.server.ServerBootStrap;
import org.apache.ntis.test.config.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.service.inventory.dao.IDataService;
import com.service.inventory.dao.Product;
import com.service.inventory.dao.config.DaoConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { NtisAwareInventoryClientConfig.class,
		DaoConfig.class,TestConfig.class })
@EnableAutoConfiguration(exclude = DispatcherServletAutoConfiguration.class)
public class NtisInventoryTest {

	@Autowired
	private NtisAwareInventoryClient webservice;

	@Autowired
	private IDataService manager;

	private ConfigurableApplicationContext context;

	@Before
	public void setup() {
		context = SpringApplication.run(ServerBootStrap.class);
	}

	@After
	public void tear() {
		if (context != null)
			context.close();
	}

	@Test
	public void test1() throws Exception {
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

		// webservice.setAddress("http://localhost:9191/inventory/service",
		// serviceName);
		// Thread.currentThread().sleep(1000 * 5);
		for(int i=1;i<5;i++){
			callservice(ids);
		}

	}
	
	private void callservice(InventoryIds ids) throws InterruptedException{
		InventoryList actualresponse = webservice.getInventory(ids);
		Thread.sleep(100);
		System.out.println("Response=" + actualresponse);

	}

}
