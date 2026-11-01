package com.ecomflow.admin.service;

import com.ecomflow.admin.dto.request.StockAdjustmentRequest;
import com.ecomflow.admin.dto.response.InventoryResponse;
import com.ecomflow.admin.entity.Subsidiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    Page<InventoryResponse> getInventory(String subsidiaryId, String keyword, Boolean isLowStock, Pageable pageable);
    InventoryResponse adjustStock(StockAdjustmentRequest request);
    List<Subsidiary> getAllSubsidiaries();
}
