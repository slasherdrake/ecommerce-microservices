package com.ecommerce.productservice.service;

import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.concurrent.TimeoutException;


import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import java.math.BigDecimal;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ProductService {
    private final ProductRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);


    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product createProduct(Product product) {
        return repo.save(product);
    }


    @CircuitBreaker(name = "productService", fallbackMethod = "getProductByIdFallback")
    public Product getProductById(Long id, String fail) throws TimeoutException{
        if ("true".equals(fail)) {
            sleep();
            throw new TimeoutException("Forced failure for testing");
        }
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Page<Product> getProducts(String category, Pageable pageable) {
        if (Objects.nonNull(category) && !category.isEmpty()) {
            return repo.findByCategory(category, pageable);
        }
        return repo.findAll(pageable);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        try {
        Product existingProduct = getProductById(id, "false");
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());
        return repo.save(existingProduct);
        } catch (TimeoutException e) {
            throw new RuntimeException("Failed to update product due to service unavailability", e);
        }
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    public Product getProductByName(String name) {
        return repo.findByName(name)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        }

    public Product updateProductByName(String name, Product updatedProduct) {
        Product existingProduct = getProductByName(name);
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());
        return repo.save(existingProduct);
    }

    public void deleteProductByName(String name) {
        repo.deleteByName(name);
    }
    public Product getProductByIdFallback(Long id, String fail, Throwable t) {
        System.out.println("CIRCUIT BREAKER ACTIVATED - Using fallback!");
        Product fallback = new Product();
        fallback.setId(id);
        fallback.setName("Fallback Product");
        fallback.setDescription("Service unavailable");
        fallback.setPrice(new BigDecimal("0.00"));
        fallback.setQuantity(0);
        fallback.setCategory("Unknown");
        return fallback;
    }

    private void sleep() throws TimeoutException{
        try {
            System.out.println("Sleep - simulating failure");
            Thread.sleep(5000);
            throw new java.util.concurrent.TimeoutException();
        } catch (InterruptedException e) {
			logger.error(e.getMessage());
        }
    }
    /*calls between orderservice eventually */
}
