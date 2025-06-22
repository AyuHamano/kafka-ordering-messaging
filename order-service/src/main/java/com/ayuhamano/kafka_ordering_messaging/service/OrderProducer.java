package com.ayuhamano.kafka_ordering_messaging.service;

import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(OrderEvent order) {
        kafkaTemplate.send("orders", order);
    }
}
