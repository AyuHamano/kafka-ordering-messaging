package com.ayuhamano.kafka_ordering_messaging.controller;

import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderEvent;
import com.ayuhamano.kafka_ordering_messaging.service.OrderProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/orders")
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

