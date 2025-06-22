package com.ayuhamano.kafka_ordering_messaging.model.dto;

// Generic response wrapper
public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation successful", data);
    }
}
