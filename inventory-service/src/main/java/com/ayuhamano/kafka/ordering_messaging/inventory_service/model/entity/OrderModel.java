package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private ProductModel product;
    private int quantity;

    public OrderModel(ProductModel product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}