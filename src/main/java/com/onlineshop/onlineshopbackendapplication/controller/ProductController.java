package com.onlineshop.onlineshopbackendapplication.controller;

import com.onlineshop.onlineshopbackendapplication.model.Product;
import com.onlineshop.onlineshopbackendapplication.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @PostMapping(path="/addProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpStatus addNewProduct(@RequestBody Product product) {
        if(productRepository.findById(product.getProduct_id()).isPresent()) {
            return HttpStatus.CONFLICT;
        }
        productRepository.save(product);
        return HttpStatus.CREATED;
    }

    @GetMapping(path="/getAllProducts")
    public @ResponseBody Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }


}
