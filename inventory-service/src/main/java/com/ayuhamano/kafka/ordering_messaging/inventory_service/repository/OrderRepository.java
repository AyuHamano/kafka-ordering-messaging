package com.ayuhamano.kafka.ordering_messaging.inventory_service.repository;


import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, UUID> {
}