package com.ayuhamano.kafka_ordering_messaging.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@Getter
@Setter
public class ProductModel {
    @Id
    private Long id;

    private String title;
    private String description;
    private String category;
    private BigDecimal price;
    private Double discountPercentage;
    private Double rating;
    private Integer stock;
    private String brand;
    private String thumbnail;
}