package com.ayuhamano.kafka_ordering_messaging.service;

import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderEvent;
import com.ayuhamano.kafka_ordering_messaging.model.dto.PurchaseDto;
import com.ayuhamano.kafka_ordering_messaging.model.entity.ProductModel;
import com.ayuhamano.kafka_ordering_messaging.repository.ProductRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(ProductRepository productRepository,
                        KafkaTemplate<String, String> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public OrderEvent processPurchase(PurchaseDto request) {
        ProductModel product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (product.getStock() < request.quantity()) {
            throw new RuntimeException("Estoque insuficiente");
        }

        product.setStock(product.getStock() - request.quantity());
        productRepository.save(product);

        OrderEvent event = new OrderEvent(
                product.getId(),
                product.getTitle(),
                request.customerName(),
                request.quantity(),
                product.getPrice(),
                product.getPrice() * request.quantity(),
                LocalDateTime.now(),
                "CONFIRMED"
        );

        kafkaTemplate.send("orders", "Compra realizada com sucesso!");

        return event;
    }
}