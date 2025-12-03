package com.ecommerce.productservice.event;

import com.ecommerce.productservice.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.function.Consumer;

@Component
public class InventoryEventConsumer {
    private final ProductService productService;
    
    public InventoryEventConsumer(ProductService productService) {
        this.productService = productService;
    }
    
    @Bean
    public Consumer<OrderCreatedEvent> consumeOrderCreated() {
        return event -> {
            event.getItems().forEach(item -> 
                productService.reduceInventory(item.getProductId(), item.getQuantity())
            );
        };
    }
}

