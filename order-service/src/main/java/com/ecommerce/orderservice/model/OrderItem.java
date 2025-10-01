package com.ecommerce.orderservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal = BigDecimal.ZERO;

   
    public Long getId() { 
        return id; 
    }
    public Order getOrder() { 
        return order; 
    }
    public void setOrder(Order order) { 
        this.order = order; 
    }
    public Long getProductId() { 
        return productId; 
    }
    public void setProductId(Long productId) { 
        this.productId = productId; 
    }
    public Integer getQuantity() { 
        return quantity; 
    }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity; 
    }
    public BigDecimal getUnitPrice() { 
        return unitPrice; 
    }
    public void setUnitPrice(BigDecimal unitPrice) { 
        this.unitPrice = unitPrice; 
    }
    public BigDecimal getLineTotal() { 
        return lineTotal; 
    }
    public void setLineTotal(BigDecimal lineTotal) { 
        this.lineTotal = lineTotal; 
    }
}
