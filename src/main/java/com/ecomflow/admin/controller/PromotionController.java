package com.ecomflow.admin.controller;

import com.ecomflow.admin.common.ApiResponse;
import com.ecomflow.admin.dto.request.AssignProductPromotionRequest;
import com.ecomflow.admin.dto.response.PromotionResponse;
import com.ecomflow.admin.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller quản lý Chiến dịch Khuyến mãi & Gán sản phẩm
 */
@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PromotionResponse>>> getPromotions(
            @PageableDefault(size = 20, sort = "saleId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<PromotionResponse> page = promotionService.getPromotions(pageable);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách chiến dịch khuyến mãi thành công", page));
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<Void>> assignProductToPromotion(
            @Valid @RequestBody AssignProductPromotionRequest request
    ) {
        promotionService.assignProductToPromotion(request);
        return ResponseEntity.ok(ApiResponse.ok("Gán sản phẩm vào chương trình khuyến mãi thành công", null));
    }
}

