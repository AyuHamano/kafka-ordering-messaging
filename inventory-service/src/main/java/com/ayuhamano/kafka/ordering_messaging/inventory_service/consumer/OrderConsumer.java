package com.ayuhamano.kafka.ordering_messaging.inventory_service.consumer;

import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderDto;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class OrderConsumer {
    private final InventoryService inventoryService;
    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);


    public OrderConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "orders")
    public void consume(OrderDto order) {
        try {
            inventoryService.processOrder(order);
            logger.info("Pedido processado com sucesso");
        } catch (Exception e) {
            logger.error("Ocorreu um erro " + e.getMessage());
        }
    }
}
