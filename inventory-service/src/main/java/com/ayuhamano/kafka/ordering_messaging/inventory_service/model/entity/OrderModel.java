package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemModel> items = new ArrayList<>();

    // Método auxiliar para adicionar itens
    public void addItem(ProductModel product, int quantity) {
        OrderItemModel item = new OrderItemModel(this, product, quantity);
        items.add(item);
    }
}