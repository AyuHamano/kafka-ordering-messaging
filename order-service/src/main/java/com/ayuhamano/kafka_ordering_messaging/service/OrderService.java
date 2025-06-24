package com.ayuhamano.kafka_ordering_messaging.service;


import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderDto;
import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderEvent;
import com.ayuhamano.kafka_ordering_messaging.model.dto.OrderItemDto;
import com.ayuhamano.kafka_ordering_messaging.model.entity.OrderItemModel;
import com.ayuhamano.kafka_ordering_messaging.model.entity.OrderModel;
import com.ayuhamano.kafka_ordering_messaging.model.entity.ProductModel;
import com.ayuhamano.kafka_ordering_messaging.repository.OrderRepository;
import com.ayuhamano.kafka_ordering_messaging.repository.ProductRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;


    public OrderService(ProductRepository productRepository,
                         OrderRepository order, OrderProducer orderProducer) {
        this.productRepository = productRepository;
        this.orderRepository = order;

        this.orderProducer = orderProducer;
    }


    @Transactional
    public OrderModel createOrder(OrderEvent orderEvent) throws ExecutionException, InterruptedException, TimeoutException {

        List<OrderItemDto> itemsDto = orderEvent.items();


        OrderModel order = new OrderModel();
        List<OrderItemModel> orderItems = new ArrayList<>();


            for (OrderItemDto itemDto : itemsDto) {
                ProductModel product = productRepository.findById(itemDto.productId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemDto.productId()));

                OrderItemModel orderItem = new OrderItemModel();
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDto.quantity());
                orderItem.setOrder(order);

                orderItems.add(orderItem);
            }

            order.setItems(orderItems);
            order.setCustomerName(orderEvent.customerName());
            order.setEmail(orderEvent.email());
            order.setCreatedAt(LocalDateTime.now());

            OrderDto orderDto = new OrderDto(order.getId(), order.getCustomerName(), order.getEmail(), orderEvent.items());

            orderProducer.sendOrder(orderDto);

            return orderRepository.save(order);

    }

}