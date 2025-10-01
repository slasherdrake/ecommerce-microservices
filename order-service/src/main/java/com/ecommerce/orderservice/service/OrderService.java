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

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductClient productClient;

    public OrderService(OrderRepository orderRepo, ProductClient productClient) {
        this.orderRepo = orderRepo;
        this.productClient = productClient;
    }

    @Transactional
    public Order create(CreateOrderRequest req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Order order = new Order();
        order.setCustomerId(req.getCustomerId());

        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderRequest.Item it : req.getItems()) {
            ProductView pv = productClient.getProduct(it.productId);
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

        // Phase 1 demo only: decrement stock in Product Service
        for (OrderItem oi : saved.getItems()) {
            productClient.adjustStock(oi.getProductId(), -oi.getQuantity());
        }

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
}

