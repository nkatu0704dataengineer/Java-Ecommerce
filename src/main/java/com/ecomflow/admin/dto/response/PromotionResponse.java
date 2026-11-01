package com.ecomflow.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {
    private String saleId;
    private String saleName;
    private BigDecimal discountValue;
    private BigDecimal minOrderValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // ACTIVE, SCHEDULED, EXPIRED
    private Integer assignedProductsCount;
}

