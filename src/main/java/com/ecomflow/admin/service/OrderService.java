package com.ecomflow.admin.service;

import com.ecomflow.admin.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderResponse> getOrders(String status, String keyword, Pageable pageable);
    OrderResponse getOrderDetail(String orderId);
    OrderResponse updateOrderStatus(String orderId, String newStatus);
}

