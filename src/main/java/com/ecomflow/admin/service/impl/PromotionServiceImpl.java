package com.ecomflow.admin.service.impl;

import com.ecomflow.admin.common.exception.BadRequestException;
import com.ecomflow.admin.common.exception.ResourceNotFoundException;
import com.ecomflow.admin.dto.request.AssignProductPromotionRequest;
import com.ecomflow.admin.dto.response.PromotionResponse;
import com.ecomflow.admin.entity.Product;
import com.ecomflow.admin.entity.ProductSale;
import com.ecomflow.admin.entity.ProductSaleId;
import com.ecomflow.admin.entity.ProgramSale;
import com.ecomflow.admin.repository.ProductRepository;
import com.ecomflow.admin.repository.ProductSaleRepository;
import com.ecomflow.admin.repository.ProgramSaleRepository;
import com.ecomflow.admin.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromotionServiceImpl implements PromotionService {

    private final ProgramSaleRepository programSaleRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<PromotionResponse> getPromotions(Pageable pageable) {
        Page<ProgramSale> page = programSaleRepository.findAll(pageable);
        LocalDate today = LocalDate.now();

        return page.map(ps -> {
            String status = "SCHEDULED";
            if (!today.isBefore(ps.getStartDate()) && !today.isAfter(ps.getEndDate())) {
                status = "ACTIVE";
            } else if (today.isAfter(ps.getEndDate())) {
                status = "EXPIRED";
            }

            int count = productSaleRepository.findByIdSaleId(ps.getSaleId()).size();

            return PromotionResponse.builder()
                    .saleId(ps.getSaleId())
                    .saleName(ps.getSaleName())
                    .discountValue(ps.getDiscountValue())
                    .minOrderValue(ps.getMinOrderValue())
                    .startDate(ps.getStartDate())
                    .endDate(ps.getEndDate())
                    .status(status)
                    .assignedProductsCount(count)
                    .build();
        });
    }

    @Override
    @Transactional
    public void assignProductToPromotion(AssignProductPromotionRequest request) {
        ProgramSale sale = programSaleRepository.findById(request.getSaleId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chiến dịch khuyến mãi: " + request.getSaleId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm: " + request.getProductId()));

        ProductSaleId id = new ProductSaleId(product.getProductId(), sale.getSaleId());
        if (productSaleRepository.existsById(id)) {
            throw new BadRequestException("Sản phẩm đã được gán vào chiến dịch này trước đó!");
        }

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ProductSale ps = ProductSale.builder()
                .id(id)
                .product(product)
                .programSale(sale)
                .createdAt(now)
                .build();

        productSaleRepository.save(ps);
    }
}
