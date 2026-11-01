package com.ecomflow.admin.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String productId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

    @NotBlank(message = "Mã ngành hàng không được để trống")
    private String categoryId;

    @NotBlank(message = "Mã thương hiệu không được để trống")
    private String brandId;

    @NotNull(message = "Giá niêm yết không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá sản phẩm phải lớn hơn 0")
    private BigDecimal productListprice;

    private Double productWeightG;
    private String productDescription;
    private String productVersion;
    private String productPhotosLink;
}
