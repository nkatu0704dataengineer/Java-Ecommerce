package com.ecomflow.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private String customerId;
    private String customerName;
    private String employeeId;
    private String employeeName;
    private String orderStatus;
    private BigDecimal totalAmount;
    private BigDecimal totalShipFee;
    private String targetAddress;
    private String targetCity;
    private String createdAt;
    private String deliveredAt;
    private PaymentResponse payment;
    private List<OrderItemResponse> items;
}
