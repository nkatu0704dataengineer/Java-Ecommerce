package com.ecomflow.admin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentRequest {

    @NotBlank(message = "Mã chi nhánh không được để trống")
    private String subsidiaryId;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String productId;

    @NotBlank(message = "Loại điều chỉnh (IN hoặc OUT) không được để trống")
    private String adjustType; // IN hoặc OUT

    @NotNull(message = "Số lượng thay đổi không được để trống")
    @Min(value = 1, message = "Số lượng thay đổi phải lớn hơn 0")
    private Integer changeQuantity;
}
