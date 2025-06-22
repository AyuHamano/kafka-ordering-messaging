package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto;

import java.util.List;

public record PurchaseDto(
        String customerName,
        List<OrderItemDto> items
) {}