package com.service.inventory.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DaoManager implements IDataService {

	@Autowired
	private ProductRepository productRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.inventory.dao.IDataService#findProductBySkid(java.lang.Long)
	 */
	@Override
	public Product findProductBySkid(Long skuid) {
		return productRepository.findOne(skuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.inventory.dao.IDataService#saveProduct(com.service.inventory
	 * .dao.Product)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Product saveProduct(Product p) {
		return productRepository.saveAndFlush(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.inventory.dao.IDataService#tableCount()
	 */
	@Override
	public long productCount() {
		return productRepository.count();
	}

	public void setProductRepository(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

}
