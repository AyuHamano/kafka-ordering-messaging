package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto;

public record OrderItemDto(
        Long productId,
        int quantity
) {}