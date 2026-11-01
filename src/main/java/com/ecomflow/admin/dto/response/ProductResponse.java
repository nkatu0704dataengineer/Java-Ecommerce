package com.ecomflow.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String productId;
    private String productName;
    private String productVersion;
    private String categoryId;
    private String categoryName;
    private String brandId;
    private String brandName;
    private BigDecimal productListprice;
    private BigDecimal avgRating;
    private Integer reviewCount;
    private String productStatus;
    private Double productWeightG;
    private String productDescription;
    private String productPhotosLink;
}
