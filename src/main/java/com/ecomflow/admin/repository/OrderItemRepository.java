package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.OrderItem;
import com.ecomflow.admin.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    @Query("SELECT oi FROM OrderItem oi " +
           "JOIN FETCH oi.product " +
           "JOIN FETCH oi.subsidiary " +
           "LEFT JOIN FETCH oi.programSale " +
           "WHERE oi.id.orderId = :orderId")
    List<OrderItem> findByOrderIdWithDetails(@Param("orderId") String orderId);
}
