package com.service.inventory.dao;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
