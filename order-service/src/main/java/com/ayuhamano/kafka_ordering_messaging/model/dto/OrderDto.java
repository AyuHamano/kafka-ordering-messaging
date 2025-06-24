package com.ayuhamano.kafka_ordering_messaging.model.dto;

import java.util.List;
import java.util.UUID;

public record OrderDto(UUID id,
                       String customerName,
                       String email,
                       List<OrderItemDto> items) {
}
