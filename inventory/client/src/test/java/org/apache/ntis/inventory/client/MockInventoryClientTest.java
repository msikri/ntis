package org.apache.ntis.inventory.client;

import static org.junit.Assert.assertNotNull;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;

import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.inventory.model.InventoryStatusType;
import org.apache.inventory.model.InventoryType;
import org.apache.inventory.model.ObjectFactory;
import org.apache.ntis.application.test.TestApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.client.MockWebServiceServer;

import com.service.inventory.client.InventoryClient;
import com.service.inventory.client.config.InventoryClientConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { InventoryClientConfig.class,
		TestApplication.class })
public class MockInventoryClientTest {

    @Autowired
    private InventoryClient client;

    @Autowired
    private WebServiceTemplate template;

    private MockWebServiceServer mockServer;

    @Before
    public void createServer() throws Exception {

        mockServer = MockWebServiceServer.createServer(template);
    }

    @Test
    public void testProduct() throws Exception {

        ObjectFactory factory = new ObjectFactory();
        InventoryIds ids = factory.createInventoryIds();
        ids.getSkuids().add(1234L);
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
        invent.setName("test");
        invent.setPrice(7);
        invent.setStatus(InventoryStatusType.IN_STOCK);
        responselist.getInventory().add(invent);

        JAXBSource responsesource = new JAXBSource(context, responselist);

        mockServer.expect(payload(source)).andRespond(
                withPayload(responsesource));

        assertNotNull(client);
        assertNotNull(client.getServiceName());

        InventoryIds skuIds = factory.createInventoryIds();
        skuIds.getSkuids().add(1234L);
        client.setAddress("http://www.inventory.com", "mockserver");
        InventoryList resp = client.getInventory(skuIds);
        assertNotNull(resp);

    }
}
