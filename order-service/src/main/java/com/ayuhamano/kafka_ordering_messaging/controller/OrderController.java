package com.ayuhamano.kafka_ordering_messaging.controller;

import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderEvent;
import com.ayuhamano.kafka_ordering_messaging.model.dto.PurchaseDto;
import com.ayuhamano.kafka_ordering_messaging.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderEvent> createOrder(@RequestBody PurchaseDto request) {
        OrderEvent event = orderService.processPurchase(request);
        return ResponseEntity.ok(event);
    }
}