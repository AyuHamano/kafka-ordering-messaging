package com.ayuhamano.kafka_ordering_messaging.model.entity;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductModel {
    @Id
    private Long id;

    private String title;
    private String description;
    private String category;
    private Double price;
    private Double discountPercentage;
    private Double rating;
    private Integer stock;
    private String brand;
    private String thumbnail;
}