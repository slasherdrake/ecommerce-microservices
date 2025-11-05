package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.ProductView;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderItem;
import com.ecommerce.orderservice.model.OrderStatus;
import com.ecommerce.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    // private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }
    
    @Transactional
    public Order create(CreateOrderRequest req){
        System.out.println("OrderService.create() called with request: " + req);
        
        if (req.getItems() == null || req.getItems().isEmpty()) {
            System.out.println("Order creation failed: No items provided");
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        System.out.println("Creating order for customer: " + req.getCustomerId());
        Order order = new Order();
        order.setCustomerId(req.getCustomerId());

        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderRequest.Item it : req.getItems()) {
            System.out.println("Processing item: productId=" + it.productId + ", quantity=" + it.quantity);
            
            // Get JWT token and make direct HTTP call to product service
            org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            if (auth instanceof org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken jwtAuth) {
                String tokenValue = jwtAuth.getToken().getTokenValue();
                headers.set("Authorization", "Bearer " + tokenValue);
                System.out.println("OrderService: Adding Authorization header for product request");
            }
            
            org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(headers);
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            
            ProductView pv;
            try {
                pv = restTemplate.exchange("http://product-service:8081/api/products/{id}", 
                    org.springframework.http.HttpMethod.GET, entity, ProductView.class, it.productId).getBody();
                System.out.println("OrderService: Got product: " + pv);
            } catch (Exception e) {
                System.out.println("OrderService: Error getting product: " + e.getMessage());
                pv = null;
            }
            if (pv == null) {
                order.setStatus(OrderStatus.CANCELED);
                throw new RuntimeException("Product " + it.productId + " not found");
            }
            if (pv.getQuantity() == null || pv.getQuantity() < it.quantity) {
                order.setStatus(OrderStatus.CANCELED);
                throw new IllegalArgumentException("Insufficient stock for product " + it.productId);
            }

            OrderItem oi = new OrderItem();
            oi.setProductId(it.productId);
            oi.setQuantity(it.quantity);
            oi.setUnitPrice(pv.getPrice());
            oi.setLineTotal(pv.getPrice().multiply(BigDecimal.valueOf(it.quantity)));

            order.addItem(oi);
            total = total.add(oi.getLineTotal());
        }

        order.setTotal(total);
        order.setStatus(OrderStatus.CONFIRMED);
        Order saved = orderRepo.save(order);

        // Stock adjustment removed for simplicity

        return saved;
    }

    public Order get(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }


    public void delete(Long id) {
        if (!orderRepo.existsById(id)) {
            throw new RuntimeException("Order " + id + " not found");
        }
        orderRepo.deleteById(id);
    }

    // public Order CreateFallback(CreateOrderRequest req, String fail, Throwable t) {
    //     logger.warn("Circuit breaker activated for order creation: {}", t.getMessage());
        
    //     Order fallback = new Order();
    //     fallback.setCustomerId(req.getCustomerId());
    //     fallback.setStatus(OrderStatus.CANCELED);
    //     fallback.setTotal(BigDecimal.ZERO);
        
    //     return orderRepo.save(fallback);
    // }

    // private void sleep() throws TimeoutException{
    //     try {
    //         System.out.println("Sleep - simulating failure");
    //         Thread.sleep(5000);
    //         throw new java.util.concurrent.TimeoutException();
    //     } catch (InterruptedException e) {
	// 		logger.error(e.getMessage());
    //     }
    // }
}

