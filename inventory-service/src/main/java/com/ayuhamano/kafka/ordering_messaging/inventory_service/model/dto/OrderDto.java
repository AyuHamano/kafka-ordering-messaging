package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto;

import java.util.List;
import java.util.UUID;

public record OrderDto(  UUID id,
        String customerName,
         String email,
        List<OrderItemDto> items) {
}
