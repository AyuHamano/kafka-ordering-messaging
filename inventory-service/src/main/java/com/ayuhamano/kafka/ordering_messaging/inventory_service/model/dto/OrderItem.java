package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto;

import java.math.BigDecimal;

public record OrderItem(
        Long productId,
        String productTitle,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
