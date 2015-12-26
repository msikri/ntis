package org.apache.ntis.inventory.service.config;

import org.apache.inventory.config.InterfaceConfig;
import org.apache.ntis.inventory.service.InventoryServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;

@Configuration
@ComponentScan(basePackages = { "com.service.inventory.dao.config",
		"org.apache.inventory.config" })
public class ServiceConfig extends InterfaceConfig {

	@Bean
	@Order(value = 1)
	public InventoryServiceImpl service() {
		return new InventoryServiceImpl();
	}

	@Bean
	@Order(value = 2)
	public MessageDispatcherServlet dispatcherServlet1() {
		return new MessageDispatcherServlet();
	}

	@Bean
	public PayloadRootAnnotationMethodEndpointMapping endpoint() {
		PayloadRootAnnotationMethodEndpointMapping mapping = new PayloadRootAnnotationMethodEndpointMapping() {

//			@Override
//			protected void initApplicationContext() throws BeansException {
//				getApplicationContext().getBean(InventoryServiceImpl.class);
//				super.initApplicationContext();
//			}

		};
		return mapping;
	}

	// wsdl at http://localhost:8080/inventory/service/inventory.wsdl

	@Bean
	public DefaultWsdl11Definition inventory() {
		DefaultWsdl11Definition def = new DefaultWsdl11Definition();
		def.setSchema(inventoryschema());
		def.setPortTypeName("InventoryService");
		def.setLocationUri("/inventory/service");
		def.setTargetNamespace("http://www.ntis.org/inventory");
		return def;
	}
}
