package com.ecomflow.admin.service;

import com.ecomflow.admin.dto.request.AssignProductPromotionRequest;
import com.ecomflow.admin.dto.response.PromotionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    Page<PromotionResponse> getPromotions(Pageable pageable);
    void assignProductToPromotion(AssignProductPromotionRequest request);
}

