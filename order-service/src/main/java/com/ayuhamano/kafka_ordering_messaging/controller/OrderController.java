package com.ayuhamano.kafka_ordering_messaging.controller;

import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderEvent;
import com.ayuhamano.kafka_ordering_messaging.service.OrderProducer;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderProducer orderProducer;


    public OrderController(
                           OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping
    public OrderEvent createOrder(@RequestBody OrderEvent orderEvent) {
        OrderEvent order = new OrderEvent( orderEvent.customerName(), orderEvent.email(), orderEvent.items());
        orderProducer.sendOrder(order);
        return order;
    }

}

