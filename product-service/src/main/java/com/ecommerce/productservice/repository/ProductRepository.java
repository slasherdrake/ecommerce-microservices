package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;  
import java.util.Optional;
import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(String category, Pageable pageable);
    Optional<Product> findByName(String name);

    @Transactional
    void deleteByName(String name);
    
}
