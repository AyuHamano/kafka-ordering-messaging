package com.ayuhamano.kafka_ordering_messaging.model.dto;


import java.util.List;

public record OrderEvent(
        String customerName,
        String email,
        List<OrderItemDto> items
) {}