package com.ayuhamano.kafka.ordering_messaging.inventory_service.repository;

import com.ayuhamano.kafka.ordering_messaging.inventory_service.model.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;


@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    @Query("SELECT CASE WHEN (p.stock >= :quantity) THEN true ELSE false END " +
            "FROM ProductModel p WHERE p.id = :productId")
    boolean hasSufficientStock(@Param("productId") Long productId,
                               @Param("quantity") int quantity);


    @Modifying
    @Query("UPDATE ProductModel p SET p.stock = p.stock - :quantity " +
            "WHERE p.id = :productId AND p.stock >= :quantity")
    int decreaseStock(@Param("productId") Long productId,
                      @Param("quantity") int quantity);
}
