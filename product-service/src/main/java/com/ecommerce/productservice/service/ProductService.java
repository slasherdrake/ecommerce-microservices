package com.ecommerce.productservice.service;

import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product createProduct(Product product) {
        return repo.save(product);
    }

    public Product getProductById(Long id) {
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
        Product existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());
        return repo.save(existingProduct);
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

    /*calls between orderservice eventually */
}
