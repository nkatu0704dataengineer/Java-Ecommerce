package com.ecomflow.admin.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity ánh xạ bảng dbo.product_sales (4.356 dòng danh sách sản phẩm hưởng khuyến mãi)
 */
@Entity
@Table(name = "product_sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSale {

    @EmbeddedId
    private ProductSaleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("saleId")
    @JoinColumn(name = "sale_id", insertable = false, updatable = false)
    private ProgramSale programSale;

    @Column(name = "created_at", length = 50, nullable = false)
    private String createdAt;

    @Column(name = "updated_at", length = 50)
    private String updatedAt;
}
