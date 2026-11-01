package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity ánh xạ bảng dbo.categories (8 dòng dữ liệu thực tế)
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseAuditEntity {

    @Id
    @Column(name = "cate_id", length = 32, nullable = false)
    private String cateId;

    @Column(name = "cate_name", length = 100, nullable = false)
    private String cateName;
}
