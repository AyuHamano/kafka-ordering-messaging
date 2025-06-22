package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto;


import java.util.List;

public record OrderEvent(
        String customerName,
        String email,
        List<OrderItemDto> items
) {}