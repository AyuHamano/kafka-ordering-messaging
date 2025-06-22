package com.ayuhamano.kafka.ordering_messaging.inventory_service.consumer;

import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderEvent;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.OrderModel;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private final InventoryService inventoryService;

    public OrderConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "orders",        groupId = "inventory-group"
    )
    public void consume(OrderEvent orderEvent) {
        try {
            OrderModel order = inventoryService.processOrder(orderEvent);
            inventoryService.sendConfirmationNotification(orderEvent.email(), "Order processed successfully: ");
        } catch (Exception e) {
            inventoryService.sendConfirmationNotification(orderEvent.email(), "Order processing failed: " + e.getMessage());
        }
    }
}
