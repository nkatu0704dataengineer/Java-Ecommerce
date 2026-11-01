package com.ecomflow.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity ánh xạ bảng dbo.product_scores (712 dòng - Quan hệ 1:1 với Product)
 * LƯU Ý ĐẶC BIỆT: Bảng này CHỈ CÓ ĐÚNG 3 CỘT, KHÔNG có created_at và updated_at.
 */
@Entity
@Table(name = "product_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductScore {

    @Id
    @Column(name = "product_id", length = 32, nullable = false)
    private String productId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount;

    @Column(name = "avg_rating", precision = 5, scale = 2, nullable = false)
    private BigDecimal avgRating;
}

