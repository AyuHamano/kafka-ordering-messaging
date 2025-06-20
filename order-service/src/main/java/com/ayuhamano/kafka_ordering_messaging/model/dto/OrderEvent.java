package com.ayuhamano.kafka_ordering_messaging.model.dto;

import java.time.LocalDateTime;

public record OrderEvent(
        Long productId,
        String productName,
        String customerName,
        Integer quantity,
        Double unitPrice,
        Double totalPrice,
        LocalDateTime orderDate,
        String status
) {}