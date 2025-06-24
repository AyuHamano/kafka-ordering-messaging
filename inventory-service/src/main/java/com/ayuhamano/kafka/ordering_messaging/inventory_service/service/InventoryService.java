package com.ayuhamano.kafka.ordering_messaging.inventory_service.service;

import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.NotificationDto;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderDto;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderItemDto;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.Status;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.OrderModel;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.ProductModel;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.repository.OrderRepository;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.repository.ProductRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class InventoryService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    public InventoryService(ProductRepository productRepository,
                            KafkaTemplate<String, NotificationDto> kafkaTemplate, OrderRepository order) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = order;
    }

    @Retryable(value = {Exception.class})
    public void sendConfirmationNotification(NotificationDto notificationDto) {
        try {

            kafkaTemplate.send("inventory-events", notificationDto).get(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.out.println("Error sending notification " + e.getMessage());
        }
    }


    @Transactional
    public void processOrder(OrderDto order) {
        if (order.items().isEmpty()) {
            throw new IllegalArgumentException("At least one item per order is required");
        }

        List<OrderItemDto> itemsDto = order.items();

        for (OrderItemDto itemDto : itemsDto) {
            ProductModel product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemDto.productId()));

            if (product.getStock() < itemDto.quantity()) {

                try {
                     orderRepository.deleteById(order.id());
                     orderRepository.flush();
                } catch (Exception e) {
                    System.out.println("Erro ao deletar pedido: " + e.getMessage());
                }
                NotificationDto failed = new NotificationDto(order.email(), order.customerName() + ", o pedido não pôde ser finalizado pois não há estoque para o produto: " + product.getTitle() + ". Tente novamente.", Status.FAILED);
                this.sendConfirmationNotification(failed);
                throw new IllegalArgumentException(order.customerName() + ", não há estoque para seu pedido");
            }
            else {
                product.setStock(product.getStock() - itemDto.quantity());
                productRepository.save(product);
            }
        }

        NotificationDto success = new NotificationDto(order.email(), order.customerName() + ", seu pedido  " + order.id() + " foi realizado com sucesso!", Status.SUCCESS);
        this.sendConfirmationNotification(success);

    }

}