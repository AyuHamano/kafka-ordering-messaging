package com.ayuhamano.kafka_ordering_messaging.service;

import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class OrderProducer {
    private final KafkaTemplate<String, OrderDto> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(OrderProducer.class);

    public OrderProducer(KafkaTemplate<String, OrderDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendOrder(OrderDto order) throws ExecutionException, InterruptedException, TimeoutException {
        try{
            kafkaTemplate.send("orders", order.id().toString(), order).get(5, TimeUnit.SECONDS);
            logger.info("Pedido {} enviado com sucesso", order.id());

        } catch (Exception e) {
            logger.error(("Erro ao enviar pedido: " +  e.getMessage()));
            throw e;
        }
    }

    @Recover
    public void sendOrderToDlq(Exception e, OrderDto order) {
        logger.error("Enviando pedido {} para DLQ após falhas: {}", order.id(), e.getMessage());
        kafkaTemplate.send("orders.DLQ", order.id().toString(), order);
    }
}
