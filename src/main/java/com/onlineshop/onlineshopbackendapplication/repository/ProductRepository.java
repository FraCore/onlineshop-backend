package com.onlineshop.onlineshopbackendapplication.repository;

import com.onlineshop.onlineshopbackendapplication.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
