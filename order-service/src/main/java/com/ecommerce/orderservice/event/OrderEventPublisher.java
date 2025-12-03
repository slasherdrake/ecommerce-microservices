package com.ecommerce.orderservice.event;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private final StreamBridge streamBridge;
    
    public OrderEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }
    
    public void publishOrderCreated(OrderCreatedEvent event) {
        streamBridge.send("orderCreated-out-0", event);
    }
}

