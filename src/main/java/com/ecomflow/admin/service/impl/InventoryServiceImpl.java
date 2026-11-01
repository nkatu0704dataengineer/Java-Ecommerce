package com.ecomflow.admin.service.impl;

import com.ecomflow.admin.common.exception.BadRequestException;
import com.ecomflow.admin.common.exception.ResourceNotFoundException;
import com.ecomflow.admin.dto.request.StockAdjustmentRequest;
import com.ecomflow.admin.dto.response.InventoryResponse;
import com.ecomflow.admin.entity.*;
import com.ecomflow.admin.repository.ProductRepository;
import com.ecomflow.admin.repository.SubProductRepository;
import com.ecomflow.admin.repository.SubsidiaryRepository;
import com.ecomflow.admin.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final SubProductRepository subProductRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<InventoryResponse> getInventory(String subsidiaryId, String keyword, Boolean isLowStock, Pageable pageable) {
        Page<SubProduct> page = subProductRepository.findInventory(subsidiaryId, keyword, isLowStock, pageable);
        return page.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public InventoryResponse adjustStock(StockAdjustmentRequest request) {
        SubProductId subProductId = new SubProductId(request.getSubsidiaryId(), request.getProductId());

        SubProduct subProduct = subProductRepository.findById(subProductId).orElseGet(() -> {
            Subsidiary sub = subsidiaryRepository.findById(request.getSubsidiaryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh: " + request.getSubsidiaryId()));
            Product prod = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm: " + request.getProductId()));

            return SubProduct.builder()
                    .id(subProductId)
                    .subsidiary(sub)
                    .product(prod)
                    .quantity(0)
                    .build();
        });

        int currentQty = subProduct.getQuantity() != null ? subProduct.getQuantity() : 0;
        int change = request.getChangeQuantity();

        if ("IN".equalsIgnoreCase(request.getAdjustType())) {
            subProduct.setQuantity(currentQty + change);
        } else if ("OUT".equalsIgnoreCase(request.getAdjustType())) {
            if (currentQty < change) {
                throw new BadRequestException("Số lượng tồn kho hiện tại (" + currentQty + ") không đủ để xuất (" + change + ")!");
            }
            subProduct.setQuantity(currentQty - change);
        } else {
            throw new BadRequestException("Loại điều chỉnh không hợp lệ: " + request.getAdjustType() + ". Chỉ chấp nhận IN hoặc OUT.");
        }

        SubProduct saved = subProductRepository.save(subProduct);
        return mapToResponse(saved);
    }

    @Override
    public List<Subsidiary> getAllSubsidiaries() {
        return subsidiaryRepository.findAll();
    }

    private InventoryResponse mapToResponse(SubProduct sp) {
        int qty = sp.getQuantity() != null ? sp.getQuantity() : 0;
        String status = qty == 0 ? "OUT_OF_STOCK" : (qty < 5 ? "LOW_STOCK" : "IN_STOCK");

        return InventoryResponse.builder()
                .subsidiaryId(sp.getId().getSubsidiaryId())
                .subsidiaryName(sp.getSubsidiary() != null ? sp.getSubsidiary().getSubName() : sp.getId().getSubsidiaryId())
                .subsidiaryCity(sp.getSubsidiary() != null ? sp.getSubsidiary().getSubCity() : null)
                .productId(sp.getId().getProductId())
                .productName(sp.getProduct() != null ? sp.getProduct().getProductName() : sp.getId().getProductId())
                .quantity(qty)
                .stockStatus(status)
                .updatedAt(sp.getUpdatedAt())
                .build();
    }
}
