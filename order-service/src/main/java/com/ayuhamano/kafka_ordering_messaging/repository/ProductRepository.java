package com.ayuhamano.kafka_ordering_messaging.repository;

import com.ayuhamano.kafka_ordering_messaging.model.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
}
