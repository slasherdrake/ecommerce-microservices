package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeoutException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = service.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id, @RequestParam(value = "fail",required = false) String fail) throws TimeoutException {
            Product product = service.getProductById(id, fail);
            return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = service.getProducts(category, pageable);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product updatedProduct = service.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
        @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Product product = service.getProductByName(name);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/name/{name}")
    public ResponseEntity<Product> updateProductByName(@PathVariable String name, @Valid @RequestBody Product product) {
        Product updatedProduct = service.updateProductByName(name, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteProductByName(@PathVariable String name) {
        service.deleteProductByName(name);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/adjust-stock")
    public ResponseEntity<Product> adjustStock(@PathVariable Long id, @RequestParam int delta) throws TimeoutException {
        Product product = service.getProductById(id, "false");
        int newQuantity = product.getQuantity() + delta;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        product.setQuantity(newQuantity);
        Product updated = service.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    
    }

}
