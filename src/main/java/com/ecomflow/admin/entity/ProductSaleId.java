package com.ecomflow.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Khóa phức hợp Composite PK cho bảng product_sales
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSaleId implements Serializable {

    @Column(name = "product_id", length = 32, nullable = false)
    private String productId;

    @Column(name = "sale_id", length = 32, nullable = false)
    private String saleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSaleId that = (ProductSaleId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(saleId, that.saleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, saleId);
    }
}
