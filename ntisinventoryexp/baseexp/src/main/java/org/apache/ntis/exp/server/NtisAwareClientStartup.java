package org.apache.ntis.exp.server;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;

import org.apache.commons.logging.LogFactory;
import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.inventory.model.InventoryStatusType;
import org.apache.inventory.model.InventoryType;
import org.apache.inventory.model.ObjectFactory;
import org.apache.ntis.common.LoggerFactory;
import org.apache.ntis.exp.client.NtisAwareInventoryClient;
import org.apache.ntis.logging.LogLevel;
import org.apache.ntis.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.client.WebServiceIOException;

import com.service.inventory.dao.IDataService;
import com.service.inventory.dao.Product;

@Configuration
@ComponentScan(basePackages = { "org.apache.ntis.exp.client.config",
		"com.service.inventory.dao.config" })
public class NtisAwareClientStartup implements CommandLineRunner {

	private Logger logger = LoggerFactory.getInstance().getLogger();
	
	@Autowired
	private NtisAwareInventoryClient webservice;

	@Autowired
	private IDataService manager;

	@Override
	public void run(String... args) throws Exception {
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

		while (true) {
			try {
				callservice(ids);
			} catch (WebServiceIOException e) {
				logger.message(LogLevel.ERROR, "IOException in Client",e);
				webservice.discoverInventoryAddress();
			}
		}

	}

	private void callservice(InventoryIds ids) throws InterruptedException {
		InventoryList actualresponse = webservice.getInventory(ids);
		Thread.sleep(500);
		System.out.println("Response=" + actualresponse);

	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(NtisAwareClientStartup.class, args);
	}

}
