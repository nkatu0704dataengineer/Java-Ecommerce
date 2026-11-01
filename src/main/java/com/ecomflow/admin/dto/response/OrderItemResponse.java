package com.ecomflow.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Integer orderItemId;
    private String productId;
    private String productName;
    private String subsidiaryId;
    private String subsidiaryName;
    private String saleId;
    private Integer quantity;
    private BigDecimal listPrice;
    private BigDecimal discountRate;
    private BigDecimal salePrice;
    private BigDecimal totalPrice;
    private BigDecimal shippingFee;
}

