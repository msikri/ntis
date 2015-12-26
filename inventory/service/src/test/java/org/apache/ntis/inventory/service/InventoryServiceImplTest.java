package org.apache.ntis.inventory.service;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;

import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.inventory.model.InventoryStatusType;
import org.apache.inventory.model.InventoryType;
import org.apache.inventory.model.ObjectFactory;
import org.apache.ntis.inventory.service.config.ServiceConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;

import com.service.inventory.dao.DaoManager;
import com.service.inventory.dao.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ServiceConfig.class, ServiceTestConfig.class })
public class InventoryServiceImplTest {

	@Autowired
	private ApplicationContext applicationContext;

	private MockWebServiceClient mockClient;

	@Autowired
	DaoManager daomanager;

	@Autowired
	Product product1;

	@Before
	public void createClient() {
		daomanager.saveProduct(product1);
		mockClient = MockWebServiceClient.createClient(applicationContext);
	}

	@Test
	public void testGetInventory() throws Exception {
		ObjectFactory factory = new ObjectFactory();
		InventoryIds ids = factory.createInventoryIds();
		ids.getSkuids().add(product1.getSkuid());
		JAXBContext context = JAXBContext.newInstance(InventoryIds.class,
				InventoryList.class, InventoryType.class,
				InventoryStatusType.class);
		JAXBSource source = new JAXBSource(context, ids);

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

		JAXBSource responsesource = new JAXBSource(context, responselist);

		mockClient.sendRequest(withPayload(source)).andExpect(
				payload(responsesource));

	}
}
