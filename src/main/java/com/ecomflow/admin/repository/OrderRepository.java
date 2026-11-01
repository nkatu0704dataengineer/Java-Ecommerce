package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query(value = "SELECT o FROM Order o " +
           "JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.employee " +
           "LEFT JOIN FETCH o.payment " +
           "WHERE (:status IS NULL OR o.orderStatus = :status) " +
           "AND (:keyword IS NULL OR LOWER(o.orderId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(o.customer.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(o.customer.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))",
           countQuery = "SELECT COUNT(o) FROM Order o WHERE (:status IS NULL OR o.orderStatus = :status)")
    Page<Order> searchOrders(
            @Param("status") String status,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT o FROM Order o " +
           "JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.employee " +
           "LEFT JOIN FETCH o.payment " +
           "LEFT JOIN FETCH o.items i " +
           "LEFT JOIN FETCH i.product " +
           "LEFT JOIN FETCH i.subsidiary " +
           "WHERE o.orderId = :orderId")
    Optional<Order> findOrderDetailById(@Param("orderId") String orderId);
}
