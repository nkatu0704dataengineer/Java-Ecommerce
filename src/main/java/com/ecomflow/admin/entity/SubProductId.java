package com.ecomflow.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Khóa phức hợp Composite PK cho bảng sub_products
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubProductId implements Serializable {

    @Column(name = "subsidiary_id", length = 32, nullable = false)
    private String subsidiaryId;

    @Column(name = "product_id", length = 32, nullable = false)
    private String productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubProductId that = (SubProductId) o;
        return Objects.equals(subsidiaryId, that.subsidiaryId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subsidiaryId, productId);
    }
}
