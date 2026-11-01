package com.ecomflow.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignProductPromotionRequest {

    @NotBlank(message = "Mã chiến dịch không được để trống")
    private String saleId;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String productId;
}
