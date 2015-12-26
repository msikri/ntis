package com.service.inventory.dao.config;

import java.sql.Date;

import org.apache.inventory.model.InventoryStatusType;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.service.inventory.dao.DaoManager;
import com.service.inventory.dao.Product;
import com.service.inventory.dao.ProductRepository;

@Configuration
@ComponentScan(basePackages = { "com.service.inventory.dao.config" })
public class DataStartupApplication {

	public static void main(String[] args) throws Exception {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				DataStartupApplication.class);
		context.register(DataStartupApplication.class.getClass());

		ProductRepository repository = context.getBean(ProductRepository.class);
		DaoManager daomanager = context.getBean(DaoManager.class);

		Product p = new Product();
		p.setSkuid(1L);
		p.setDateCreated(Date.valueOf("2012-03-25"));
		p.setDateCreated(Date.valueOf("2012-03-25"));
		p.setDescription("Des for Product1");
		p.setName("Product1");
		p.setPrice(10);
		p.setQuantity(1);
		p.setStatus(InventoryStatusType.IN_STOCK);
		daomanager.saveProduct(p);
		context.close();
	}
}
