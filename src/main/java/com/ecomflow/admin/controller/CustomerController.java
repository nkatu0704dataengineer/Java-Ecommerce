package com.ecomflow.admin.controller;

import com.ecomflow.admin.common.ApiResponse;
import com.ecomflow.admin.dto.response.CartResponse;
import com.ecomflow.admin.dto.response.CustomerResponse;
import com.ecomflow.admin.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller quản lý Khách hàng & Giỏ hàng
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getCustomers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String rank,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 20, sort = "accId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<CustomerResponse> page = customerService.getCustomers(keyword, rank, isActive, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách khách hàng thành công", page));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ApiResponse<CustomerResponse>> toggleCustomerActive(@PathVariable String id) {
        CustomerResponse updated = customerService.toggleCustomerActive(id);
        return ResponseEntity.ok(ApiResponse.ok("Thay đổi trạng thái kích hoạt tài khoản thành công", updated));
    }

    @GetMapping("/{id}/cart")
    public ResponseEntity<ApiResponse<CartResponse>> getCustomerCart(@PathVariable String id) {
        CartResponse cart = customerService.getCustomerCart(id);
        return ResponseEntity.ok(ApiResponse.ok("Kiểm tra giỏ hàng khách hàng thành công", cart));
    }
}

