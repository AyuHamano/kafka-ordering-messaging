package com.ayuhamano.kafka_ordering_messaging.model.dto;

public record OrderItemDto(
        Long productId,
        int quantity
) {}