package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity ánh xạ bảng dbo.products (712 sản phẩm gốc với đầy đủ thông số logistics)
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseAuditEntity {

    @Id
    @Column(name = "product_id", length = 32, nullable = false)
    private String productId;

    @Column(name = "product_version", length = 50)
    private String productVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "cate_id", nullable = false)
    private Category category;

    @Column(name = "product_name", length = 255, nullable = false)
    private String productName;

    @Column(name = "product_description", columnDefinition = "NVARCHAR(MAX)")
    private String productDescription;

    @Column(name = "product_status", length = 30, nullable = false)
    private String productStatus;

    @Column(name = "product_rating", precision = 5, scale = 2)
    private BigDecimal productRating;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "product_listprice", precision = 18, scale = 2, nullable = false)
    private BigDecimal productListprice;

    @Column(name = "product_weight_g")
    private Double productWeightG;

    @Column(name = "product_length_cm")
    private Double productLengthCm;

    @Column(name = "product_height_cm")
    private Double productHeightCm;

    @Column(name = "product_width_cm")
    private Double productWidthCm;

    @Column(name = "product_volume_cm3")
    private Double productVolumeCm3;

    @Column(name = "product_density_g_cm3")
    private Double productDensityGCm3;

    @Column(name = "product_photos_link", columnDefinition = "NVARCHAR(MAX)")
    private String productPhotosLink;

    @Column(name = "product_photos_qty")
    private Integer productPhotosQty;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductScore score;
}
