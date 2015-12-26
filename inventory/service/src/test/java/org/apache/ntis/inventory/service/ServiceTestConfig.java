package org.apache.ntis.inventory.service;

import org.apache.inventory.model.InventoryStatusType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.service.inventory.dao.Product;

@Configuration
public class ServiceTestConfig {

	@Bean
	public Product product1() {
		Product p1 = new Product();
		p1.setSkuid(1L);
		p1.setName("product1");
		p1.setDescription("Des for Product1");
		p1.setStatus(InventoryStatusType.IN_STOCK);
		p1.setPrice(10);
		return p1;
	}
	
	@Bean
	public Product product2() {
		Product p1 = new Product();
		p1.setSkuid(12345L);
		p1.setName("product2");
		p1.setDescription("Des for Product2");
		p1.setStatus(InventoryStatusType.IN_STOCK);
		p1.setPrice(14);
		return p1;
	}
}
