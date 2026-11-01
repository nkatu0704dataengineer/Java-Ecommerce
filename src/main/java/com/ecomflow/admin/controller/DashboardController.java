package com.ecomflow.admin.controller;

import com.ecomflow.admin.common.ApiResponse;
import com.ecomflow.admin.dto.response.DashboardKpiResponse;
import com.ecomflow.admin.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller cung cấp dữ liệu cho Executive Dashboard
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpis")
    public ResponseEntity<ApiResponse<DashboardKpiResponse>> getExecutiveKpis() {
        DashboardKpiResponse kpis = dashboardService.getExecutiveKpis();
        return ResponseEntity.ok(ApiResponse.ok("Lấy chỉ số KPI Dashboard thành công", kpis));
    }
}

