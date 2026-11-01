package com.ecomflow.admin.service.impl;

import com.ecomflow.admin.dto.response.DashboardKpiResponse;
import com.ecomflow.admin.repository.*;
import com.ecomflow.admin.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final SubProductRepository subProductRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public DashboardKpiResponse getExecutiveKpis() {
        BigDecimal totalRevenue = paymentRepository.calculateTotalSuccessfulRevenue();
        long totalOrders = orderRepository.count();
        long activeCustomers = customerRepository.countByIsActiveTrue();
        long lowStockCount = subProductRepository.countLowStock(5);

        // Phân tích sắc thái đánh giá (Positive, Neutral, Negative)
        Map<String, Long> sentimentDist = new HashMap<>();
        List<Object[]> sentimentResults = reviewRepository.countBySentiment();
        for (Object[] row : sentimentResults) {
            String sentiment = (String) row[0];
            Long count = (Long) row[1];
            sentimentDist.put(sentiment, count);
        }

        // Phân bổ phương thức thanh toán
        Map<String, Long> paymentDist = new HashMap<>();
        List<Object[]> paymentResults = paymentRepository.countByPaymentMethod();
        for (Object[] row : paymentResults) {
            String method = (String) row[0];
            Long count = (Long) row[1];
            paymentDist.put(method, count);
        }

        return DashboardKpiResponse.builder()
                .totalRevenue(totalRevenue)
                .totalOrders(totalOrders)
                .activeCustomers(activeCustomers)
                .lowStockCount(lowStockCount)
                .sentimentDistribution(sentimentDist)
                .paymentMethodDistribution(paymentDist)
                .build();
    }
}
