package com.ayuhamano.kafka.ordering_messaging.inventory_service.consumer;

import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderDto;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;




@Service
public class OrderConsumer {
    private final InventoryService inventoryService;


    public OrderConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "orders")
    public void consume(OrderDto order) {
        try {
            inventoryService.processOrder(order);
            inventoryService.sendConfirmationNotification(order.email(), "Order processed successfully: ");
        } catch (Exception e) {
            inventoryService.sendConfirmationNotification(order.email(), "Order processing failed: " + e.getMessage());
        }
    }
}
