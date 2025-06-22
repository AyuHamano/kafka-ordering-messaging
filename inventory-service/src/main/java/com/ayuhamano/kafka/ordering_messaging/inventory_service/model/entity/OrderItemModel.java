package com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderModel order;

    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductModel product;

    private int quantity;

    public OrderItemModel(OrderModel order, ProductModel product, int quantity) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemModel)) return false;
        return id != null && id.equals(((OrderItemModel) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}