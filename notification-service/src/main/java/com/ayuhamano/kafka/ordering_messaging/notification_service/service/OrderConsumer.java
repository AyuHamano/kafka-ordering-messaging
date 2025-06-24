package com.ayuhamano.kafka.ordering_messaging.notification_service.service;

import com.ayuhamano.kafka.ordering_messaging.notification_service.model.dto.NotificationDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private final EmailService emailService;
    public OrderConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "inventory-events", groupId = "inventory-group"
    )
    public void consume(NotificationDto notification) {
        try {
            System.out.println(notification.email() +  " " + notification.message());
            emailService.sendEmail(notification.email(), "Mensageria Kafka - Pedido realizado " + notification.status(), notification.message());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
