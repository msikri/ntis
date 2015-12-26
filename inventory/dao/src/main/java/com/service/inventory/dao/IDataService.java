package com.service.inventory.dao;

public interface IDataService {

	public Product findProductBySkid(Long skuid);

	public Product saveProduct(Product p);

	public long productCount();

}