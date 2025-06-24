package com.ayuhamano.kafka_ordering_messaging.controller;

import com.ayuhamano.kafka_ordering_messaging.model.dto.ApiResponse;
import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderDto;
import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderEvent;
import com.ayuhamano.kafka_ordering_messaging.service.OrderProducer;
import com.ayuhamano.kafka_ordering_messaging.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    public OrderController(
                           OrderProducer orderProducer, OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Object createOrder(@RequestBody OrderEvent orderEvent) {

        try {
            OrderDto order =  orderService.createOrder(orderEvent);
            System.out.println("Pedido ralizado com sucesso");
            return new ApiResponse(
                    "Seu pedido de número " + order.id() + " está sendo processado. Aguarde confirmação do status no seu email.",
                            order
                    ) ;

        } catch (Exception e) {
            return "Request Error";
        }
    }

}

