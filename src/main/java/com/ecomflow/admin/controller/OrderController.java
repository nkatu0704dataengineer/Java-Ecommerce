package com.ecomflow.admin.controller;

import com.ecomflow.admin.common.ApiResponse;
import com.ecomflow.admin.dto.response.OrderResponse;
import com.ecomflow.admin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller quản lý Đơn hàng & Đối soát thanh toán
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderResponse> page = orderService.getOrders(status, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách đơn hàng thành công", page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(@PathVariable String id) {
        OrderResponse response = orderService.getOrderDetail(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy chi tiết đơn hàng & đối soát thành công", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable String id,
            @RequestParam String newStatus
    ) {
        OrderResponse updated = orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật trạng thái đơn hàng thành công", updated));
    }
}

