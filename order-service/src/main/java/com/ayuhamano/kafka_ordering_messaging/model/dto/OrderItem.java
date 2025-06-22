package com.ayuhamano.kafka_ordering_messaging.model.dto;

import java.math.BigDecimal;

public record OrderItem(
        Long productId,
        String productTitle,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
