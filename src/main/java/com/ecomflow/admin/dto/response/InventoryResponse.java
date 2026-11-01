package com.ecomflow.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private String subsidiaryId;
    private String subsidiaryName;
    private String subsidiaryCity;
    private String productId;
    private String productName;
    private Integer quantity;
    private String stockStatus; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK
    private String updatedAt;
}

