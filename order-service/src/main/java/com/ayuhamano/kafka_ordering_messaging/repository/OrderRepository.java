package com.ayuhamano.kafka_ordering_messaging.repository;


import com.ayuhamano.kafka_ordering_messaging.model.entity.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, UUID> {
}