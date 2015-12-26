package com.service.inventory.client;

import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.inventory.model.InventoryStatusType;
import org.apache.inventory.model.InventoryType;
import org.apache.ntis.log.time.TimedClientInterceptor;
import org.apache.ntis.topology.info.TopologyDelayInterceptor;
import org.apache.ntis.topology.info.TopologyLocation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Component
public class InventoryClient implements
		org.apache.inventory.api.InventoryService, InitializingBean {

	@Value("${inventoryclient.name}")
	private String serviceName;
	
	@Autowired
	private WebServiceTemplate webServiceTemplate;

	private String address;
	
	@Override
	public InventoryList getInventory(InventoryIds skuIds) {

		return (InventoryList) webServiceTemplate.marshalSendAndReceive(
				address, skuIds);
	}

	public void setAddress(String address,String alocation) {
		this.address = address;
		location.setLocation(alocation);
	}
	
	@Autowired
	private TopologyDelayInterceptor interceptor;
	
	@Autowired TimedClientInterceptor timeintercept;
	
	@Autowired
	private TopologyLocation location;

	@Override
	public void afterPropertiesSet() throws Exception {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(InventoryIds.class, InventoryList.class,
				InventoryType.class, InventoryStatusType.class);

		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
		ClientInterceptor inter[] = new ClientInterceptor[2];
		inter[0] = timeintercept;
		inter[1] = interceptor;
		interceptor.setSource(serviceName);
		webServiceTemplate.setInterceptors(inter);
	}

//	public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
//		this.webServiceTemplate = webServiceTemplate;
//	}

	@Override
	public String getServiceName() {
		return serviceName;
	}
}
