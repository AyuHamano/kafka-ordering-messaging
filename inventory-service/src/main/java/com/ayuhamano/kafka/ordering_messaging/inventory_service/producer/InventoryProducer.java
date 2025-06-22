package com.ayuhamano.kafka.ordering_messaging.inventory_service.producer;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public InventoryProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInventoryEvent(String event) {
        kafkaTemplate.send("inventory-events", event);
    }
}