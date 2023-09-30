package com.onlineshop.onlineshopbackendapplication.controller;

import com.onlineshop.onlineshopbackendapplication.model.Product;
import com.onlineshop.onlineshopbackendapplication.model.Storage;
import com.onlineshop.onlineshopbackendapplication.repository.ProductRepository;
import com.onlineshop.onlineshopbackendapplication.repository.StorageRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StorageRepository storageRepository;

    @PostMapping(path="/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> addNewProduct(@Valid @RequestBody Product product) {
        if(isProductPresent(product.getProduct_id())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        try {
            Product savedEntity = productRepository.save(product);
            String jsonResponse = String.format("{\"id\": %d}", savedEntity.getProduct_id());
            return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);

        }catch(Exception e) {
            return new ResponseEntity<>("An error occurred",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/update/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> updateProduct(@PathVariable Integer productId,
                                                              @Valid @RequestBody Product product) {
        if(!isProductPresent(productId)) {
            return new ResponseEntity<>(String.format("Product with id: %d not found", productId), HttpStatus.NOT_FOUND);
        }
        try {
            product.setProduct_id(productId);
            Product savedProduct = productRepository.save(product);
            return new ResponseEntity<>("Product successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/addStock/{productId}/{storageId}/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProductStock(@PathVariable Integer productId,
                                                  @PathVariable Integer storageId,
                                                  @Valid @RequestBody Storage storageInput) {
        if (!isProductPresent(productId)) {
            return new ResponseEntity<>(String.format("No Product with productId: %d found", productId), HttpStatus.NOT_FOUND);
        }

        Optional<Storage> optionalStorage = storageRepository.findById(storageId);
        return optionalStorage.map(storage -> updateExistingStorage(productId, storageId, storageInput, storage)).orElseGet(() -> createNewStorage(storageInput, storageId, productId));
    }

    private boolean isProductPresent(Integer productId) {
        return productRepository.findById(productId).isPresent();
    }

    private ResponseEntity<String> updateExistingStorage(Integer productId, Integer storageId, Storage storageInput, Storage existingStorage) {
        if (!existingStorage.getProduct_id().equals(productId)) {
            return new ResponseEntity<>(String.format("Storage Id %d has productId: %d in stock and cannot store Product with id: %d",
                    storageId, existingStorage.getProduct_id(), productId), HttpStatus.BAD_REQUEST);
        }

        try {
            existingStorage.setStorage_stock(storageInput.getStorage_stock());
            storageRepository.save(existingStorage);
            return new ResponseEntity<>(String.format("New Amount of %d was deposited to storageId: %d for productId: %d",
                    existingStorage.getStorage_stock(), storageId, productId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating the storage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<String> createNewStorage(Storage storageInput, Integer storageId, Integer productId) {
        try {
            storageRepository.save(storageInput);
            return new ResponseEntity<>(String.format("Stock of %d was added to storageId: %d for productId: %d",
                    storageInput.getStorage_stock(), storageId, productId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating new storage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
