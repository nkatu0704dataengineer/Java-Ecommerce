package com.ecomflow.admin.controller;

import com.ecomflow.admin.common.ApiResponse;
import com.ecomflow.admin.dto.request.StockAdjustmentRequest;
import com.ecomflow.admin.dto.response.InventoryResponse;
import com.ecomflow.admin.entity.Subsidiary;
import com.ecomflow.admin.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller tra cứu & điều phối Tồn kho Đa Chi nhánh
 */
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<InventoryResponse>>> getInventory(
            @RequestParam(required = false) String subsidiaryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isLowStock,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<InventoryResponse> page = inventoryService.getInventory(subsidiaryId, keyword, isLowStock, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Tra cứu danh sách tồn kho thành công", page));
    }

    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<InventoryResponse>> adjustStock(@Valid @RequestBody StockAdjustmentRequest request) {
        InventoryResponse response = inventoryService.adjustStock(request);
        return ResponseEntity.ok(ApiResponse.ok("Điều phối số lượng tồn kho thành công", response));
    }

    @GetMapping("/subsidiaries")
    public ResponseEntity<ApiResponse<List<Subsidiary>>> getSubsidiaries() {
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách 51 chi nhánh thành công", inventoryService.getAllSubsidiaries()));
    }
}
