package com.ayuhamano.kafka_ordering_messaging.model.entity;

// Custom exceptions
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product not found with ID: " + productId);
    }
}
