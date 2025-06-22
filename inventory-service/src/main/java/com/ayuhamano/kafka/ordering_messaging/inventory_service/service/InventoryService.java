package com.ayuhamano.kafka.ordering_messaging.inventory_service.service;

import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.NotificationDto;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderEvent;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderItem;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.dto.OrderItemDto;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.OrderItemModel;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.OrderModel;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.ProductModel;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.repository.OrderRepository;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.repository.ProductRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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

    public void sendConfirmationNotification(String email, String message) {
        NotificationDto notification = new NotificationDto(email, message);
        kafkaTemplate.send("inventory-events", notification);
    }


    @Transactional
    public OrderModel processOrder(OrderEvent orderEvent) {
        if (orderEvent.items().size() != 1) {
            throw new IllegalArgumentException("Only one item per order is allowed");
        }

        OrderItemDto itemDto = orderEvent.items().get(0);

        if (!hasSufficientStock(List.of(itemDto))) {
            throw new IllegalStateException("Insufficient stock");
        }

        ProductModel product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setStock(product.getStock() - itemDto.quantity());
        productRepository.save(product);

        OrderModel order = new OrderModel();
        order.setQuantity(1);
        order.setProduct(product);

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public boolean hasSufficientStock(List<OrderItemDto> items) {
        if (items == null || items.isEmpty()) {
            return false;
        }
        return items.stream().allMatch(item ->
                productRepository.hasSufficientStock(item.productId(), item.quantity())
        );
    }
}