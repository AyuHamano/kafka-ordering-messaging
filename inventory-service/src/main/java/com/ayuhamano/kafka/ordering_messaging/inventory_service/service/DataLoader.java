package com.ayuhamano.kafka.ordering_messaging.inventory_service.service;

import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.ProductModel;
import com.ayuhamano.kafka.ordering_messaging.inventory_service.repository.ProductRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DataLoader {

    @PersistenceContext
    private EntityManager entityManager;

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;

    @Autowired
    public DataLoader(RestTemplateBuilder restTemplateBuilder, ProductRepository productRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.productRepository = productRepository;
    }

    @PostConstruct
    @Transactional
    public void loadData() {
        boolean tableExists = false;
        try {
            tableExists = !entityManager.createNativeQuery(
                            "SELECT 1 FROM products LIMIT 1")
                    .getResultList().isEmpty();
        } catch (Exception e) {
        }

        if (!tableExists && productRepository.count() == 0) {
            String apiUrl = "https://dummyjson.com/products";
            ApiResponse response = restTemplate.getForObject(apiUrl, ApiResponse.class);

            if (response != null && response.getProducts() != null) {

                productRepository.saveAll(response.getProducts());
                System.out.println("Dados básicos de produtos carregados com sucesso!");
            }
        }
    }

    private static class ApiResponse {
        // Getters e Setters
        @Setter
        @Getter
        @JsonProperty("products")
        private List<ProductModel> products;

        private Integer total;
        private Integer skip;
        private Integer limit;

    }
}
