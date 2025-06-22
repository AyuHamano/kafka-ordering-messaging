package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto;

// Custom exceptions
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product not found with ID: " + productId);
    }
}
