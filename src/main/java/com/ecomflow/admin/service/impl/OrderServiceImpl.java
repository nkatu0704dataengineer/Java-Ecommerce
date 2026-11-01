package com.ecomflow.admin.service.impl;

import com.ecomflow.admin.common.exception.ResourceNotFoundException;
import com.ecomflow.admin.dto.response.OrderItemResponse;
import com.ecomflow.admin.dto.response.OrderResponse;
import com.ecomflow.admin.dto.response.PaymentResponse;
import com.ecomflow.admin.entity.Order;
import com.ecomflow.admin.entity.OrderItem;
import com.ecomflow.admin.entity.Payment;
import com.ecomflow.admin.repository.OrderRepository;
import com.ecomflow.admin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Page<OrderResponse> getOrders(String status, String keyword, Pageable pageable) {
        Page<Order> orders = orderRepository.searchOrders(status, keyword, pageable);
        return orders.map(this::mapToSummaryResponse);
    }

    @Override
    public OrderResponse getOrderDetail(String orderId) {
        Order order = orderRepository.findOrderDetailById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng có mã: " + orderId));
        return mapToDetailResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(String orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng: " + orderId));
        order.setOrderStatus(newStatus);
        Order saved = orderRepository.save(order);
        return mapToDetailResponse(saved);
    }

    private OrderResponse mapToSummaryResponse(Order o) {
        return OrderResponse.builder()
                .orderId(o.getOrderId())
                .customerId(o.getCustomer() != null ? o.getCustomer().getAccId() : null)
                .customerName(o.getCustomer() != null ? o.getCustomer().getFullName() : null)
                .employeeId(o.getEmployee() != null ? o.getEmployee().getEmployeeId() : null)
                .employeeName(o.getEmployee() != null ? o.getEmployee().getFullName() : null)
                .orderStatus(o.getOrderStatus())
                .totalAmount(o.getTotalAmount())
                .totalShipFee(o.getTotalShipFee())
                .targetAddress(o.getFullTargetAddress())
                .targetCity(o.getTargetCity())
                .createdAt(o.getCreatedAt())
                .deliveredAt(o.getDeliveredAt())
                .payment(mapPayment(o.getPayment()))
                .build();
    }

    private OrderResponse mapToDetailResponse(Order o) {
        OrderResponse response = mapToSummaryResponse(o);

        List<OrderItemResponse> itemResponses = o.getItems().stream().map(i -> OrderItemResponse.builder()
                .orderItemId(i.getId().getOrderItemId())
                .productId(i.getProduct() != null ? i.getProduct().getProductId() : null)
                .productName(i.getProduct() != null ? i.getProduct().getProductName() : null)
                .subsidiaryId(i.getSubsidiary() != null ? i.getSubsidiary().getSubId() : null)
                .subsidiaryName(i.getSubsidiary() != null ? i.getSubsidiary().getSubName() : null)
                .saleId(i.getProgramSale() != null ? i.getProgramSale().getSaleId() : null)
                .quantity(i.getQuantity())
                .listPrice(i.getListPrice())
                .discountRate(i.getDiscountRate())
                .salePrice(i.getSalePrice())
                .totalPrice(i.getTotalPrice())
                .shippingFee(i.getShippingFee())
                .build()).collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }

    private PaymentResponse mapPayment(Payment p) {
        if (p == null) return null;
        return PaymentResponse.builder()
                .paymentId(p.getPaymentId())
                .orderId(p.getOrder() != null ? p.getOrder().getOrderId() : null)
                .paymentMethod(p.getPaymentMethod())
                .paymentStatus(p.getPaymentStatus())
                .paymentAmount(p.getPaymentAmount())
                .paidAt(p.getPaidAt())
                .refundAt(p.getRefundAt())
                .build();
    }
}

