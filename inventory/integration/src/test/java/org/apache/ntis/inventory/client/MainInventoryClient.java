package org.apache.ntis.inventory.client;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;

import org.apache.inventory.api.InventoryService;
import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.inventory.model.InventoryStatusType;
import org.apache.inventory.model.InventoryType;
import org.apache.inventory.model.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.service.inventory.client.InventoryClient;
import com.service.inventory.dao.IDataService;
import com.service.inventory.dao.Product;

@Configuration
@EnableAutoConfiguration(exclude = DispatcherServletAutoConfiguration.class)
@ComponentScan(basePackages = { "org.apache.ntis.inventory.service.config",
		"com.service.inventory.client.config" })
public class MainInventoryClient implements CommandLineRunner {

	@Autowired
	private InventoryClient webservice;

	@Autowired
	private IDataService manager;
	
	@Autowired
	private InventoryService service;

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
				service.getServiceName());
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

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(
				MainInventoryClient.class, args);
		System.out.println("Completed");
		context.close();
	}

	@Override
	public void run(String... args) throws Exception {
		getHelloService();
	}
}
