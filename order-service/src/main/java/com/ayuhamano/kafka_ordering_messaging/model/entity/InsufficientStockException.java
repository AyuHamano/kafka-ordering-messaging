package com.ayuhamano.kafka_ordering_messaging.model.entity;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long productId, int available, int requested) {
        super(String.format("Insufficient stock for product %d. Available: %d, Requested: %d",
                productId, available, requested));
    }
}
