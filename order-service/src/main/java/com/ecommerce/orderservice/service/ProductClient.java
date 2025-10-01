package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.ProductView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProductClient(RestTemplate restTemplate,
                         @Value("${product.service.base-url:http://localhost:8081/api}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ProductView getProduct(Long id) {
        try {
            return restTemplate.getForObject(baseUrl + "/products/{id}", ProductView.class, id);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    /** Optional helper for Phase 1 to decrement stock after order creation */
    public void adjustStock(Long productId, int delta) {
        restTemplate.postForObject(baseUrl + "/products/{id}/adjust-stock?delta={delta}",
                null, ProductView.class, productId, delta);
    }
}
