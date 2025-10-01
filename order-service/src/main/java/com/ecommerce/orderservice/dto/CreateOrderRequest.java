package com.ecommerce.orderservice.dto;

import java.util.List;

public class CreateOrderRequest {
    private Long customerId;
    private List<Item> items;

    public static class Item {
        public Long productId;
        public Integer quantity;
    }

    public Long getCustomerId() { 
        return customerId; 
    }
    public void setCustomerId(Long customerId) { 
        this.customerId = customerId; 
    }
    public List<Item> getItems() { 
        return items; 
    }
    public void setItems(List<Item> items) { 
        this.items = items; 
    }
}

