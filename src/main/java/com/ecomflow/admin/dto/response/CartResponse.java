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
public class CartResponse {
    private String cartId;
    private String customerId;
    private String customerName;
    private String cartStatus;
    private Integer totalItems;
    private BigDecimal totalAmount;
    private List<CartItemResponse> items;
}

