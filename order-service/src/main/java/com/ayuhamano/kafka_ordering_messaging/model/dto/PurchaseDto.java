package com.ayuhamano.kafka_ordering_messaging.model.dto;

import java.io.Serializable;
import java.util.List;

public record PurchaseDto(
        String customerName,
        String customerEmail,
        List<OrderItemDto> items
) implements Serializable {}