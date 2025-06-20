package com.ayuhamano.kafka_ordering_messaging.model.dto;

public record PurchaseDto(
        Long productId,
        String customerName,
        Integer quantity
) {}