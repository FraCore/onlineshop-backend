package com.onlineshop.onlineshopbackendapplication.controller;

import com.onlineshop.onlineshopbackendapplication.model.Product;
import com.onlineshop.onlineshopbackendapplication.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @PostMapping(path="/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> addNewProduct(@Valid @RequestBody Product product) {
        if(productRepository.findById(product.getProduct_id()).isPresent()) {
            return new ResponseEntity<>("Product already exists", HttpStatus.CONFLICT);
        }
        try {
            productRepository.save(product);
            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);

        }catch(Exception e) {
            return new ResponseEntity<>("An error occurred",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> updateProduct(@PathVariable Integer id,@Valid @RequestBody Product product) {
        if(productRepository.findById(id).isPresent()) {
            try {
                product.setProduct_id(id);
                productRepository.save(product);
                return new ResponseEntity<>("Product successfully updated", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("An error occurred",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(String.format("Product with id: %d not found", id), HttpStatus.NOT_FOUND);
    }
}
