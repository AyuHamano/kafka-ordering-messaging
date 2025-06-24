package com.ayuhamano.kafka_ordering_messaging.controller;

import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderEvent;
import com.ayuhamano.kafka_ordering_messaging.service.OrderProducer;
import com.ayuhamano.kafka_ordering_messaging.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;


    public OrderController(
                           OrderProducer orderProducer, OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Object createOrder(@RequestBody OrderEvent orderEvent) {

        try {
            OrderEvent order = new OrderEvent( orderEvent.customerName(), orderEvent.email(), orderEvent.items());
            orderService.createOrder(order);
            return order;
        } catch (Exception e) {
            return "Request Error";
        }

    }

}

