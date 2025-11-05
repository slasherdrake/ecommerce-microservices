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
            System.out.println("ProductClient: Making request to " + baseUrl + "/products/" + id);
            
            // Get JWT token from SecurityContext
            org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            if (auth instanceof org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken jwtAuth) {
                String tokenValue = jwtAuth.getToken().getTokenValue();
                headers.set("Authorization", "Bearer " + tokenValue);
                System.out.println("ProductClient: Adding Authorization header");
            }
            
            org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(headers);
            ProductView result = restTemplate.exchange(baseUrl + "/products/{id}", 
                org.springframework.http.HttpMethod.GET, entity, ProductView.class, id).getBody();
            System.out.println("ProductClient: Successfully got product: " + result);
            return result;
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("ProductClient: Product not found: " + id);
            return null;
        } catch (Exception e) {
            System.out.println("ProductClient: Error calling product service: " + e.getMessage());
            throw e;
        }
    }

    /** Optional helper for Phase 1 to decrement stock after order creation */
    public void adjustStock(Long productId, int delta) {
        restTemplate.postForObject(baseUrl + "/products/{id}/adjust-stock?delta={delta}",
                null, ProductView.class, productId, delta);
    }
}
