package com.ecommerce.orderservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    
    public Long getId() { 
        return id;
    }
    public Long getCustomerId() { 
        return customerId; 
    }
    public void setCustomerId(Long customerId) { 
        this.customerId = customerId; 
    }
    public OrderStatus getStatus() { 
        return status; 
    }
    public void setStatus(OrderStatus status) { 
        this.status = status; 
    }
    public Instant getCreatedAt() { 
        return createdAt; 
    }
    public BigDecimal getTotal() { 
        return total; 
    }
    public void setTotal(BigDecimal total) { 
        this.total = total; 
    }
    public List<OrderItem> getItems() { 
        return items; 
    }

    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
    }
}
