package com.ayuhamano.kafka_ordering_messaging.service;

import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderDto;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class OrderProducer {
    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Retryable(value = {Exception.class})
    public void sendOrder(OrderDto order) throws ExecutionException, InterruptedException, TimeoutException {
        try{
            //tolerância à falha de 5 s
            kafkaTemplate.send("orders", order).get(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.out.println(("Erro ao enviar pedido: " +  e.getMessage()));
            throw e;
        }


    }
}
