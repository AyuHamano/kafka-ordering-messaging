package com.ayuhamano.kafka.ordering_messaging.inventory_service.consumer;

import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderEvent;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.OrderModel;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.producer.InventoryProducer;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;

    public OrderConsumer(InventoryService inventoryService, InventoryProducer inventoryProducer) {
        this.inventoryService = inventoryService;
        this.inventoryProducer = inventoryProducer;
    }

    @KafkaListener(topics = "orders",        groupId = "inventory-group"
    )
    public void consume(OrderEvent orderEvent) {
        try {
            OrderModel order = inventoryService.processOrder(orderEvent);
            inventoryProducer.sendInventoryEvent("Order processed successfully: ");
        } catch (Exception e) {
            inventoryProducer.sendInventoryEvent("Order processing failed: " + e.getMessage());
        }
    }
}
