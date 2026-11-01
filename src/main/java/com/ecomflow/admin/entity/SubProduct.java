package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * Entity ánh xạ bảng dbo.sub_products (28.990 dòng ma trận kho - sản phẩm)
 */
@Entity
@Table(name = "sub_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubProduct extends BaseAuditEntity {

    @EmbeddedId
    private SubProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subsidiaryId")
    @JoinColumn(name = "subsidiary_id", insertable = false, updatable = false)
    private Subsidiary subsidiary;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Min(value = 0, message = "Số lượng tồn kho phải lớn hơn hoặc bằng 0")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}

