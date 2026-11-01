package com.ecomflow.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKpiResponse {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long activeCustomers;
    private Long lowStockCount;
    private Map<String, Long> sentimentDistribution;
    private Map<String, Long> paymentMethodDistribution;
}

