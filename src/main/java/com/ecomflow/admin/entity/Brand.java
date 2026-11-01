package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity ánh xạ bảng dbo.brands (61 dòng dữ liệu thực tế)
 */
@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand extends BaseAuditEntity {

    @Id
    @Column(name = "brand_id", length = 32, nullable = false)
    private String brandId;

    @Column(name = "brand_name", length = 100, nullable = false)
    private String brandName;

    @Column(name = "brand_country", length = 50)
    private String brandCountry;
}
