package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity ánh xạ bảng dbo.program_sales (80 chiến dịch ưu đãi)
 */
@Entity
@Table(name = "program_sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramSale extends BaseAuditEntity {

    @Id
    @Column(name = "sale_id", length = 32, nullable = false)
    private String saleId;

    @Column(name = "sale_name", length = 150, nullable = false)
    private String saleName;

    @Column(name = "discount_value", precision = 9, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @Column(name = "min_ordervalue", precision = 18, scale = 2, nullable = false)
    private BigDecimal minOrderValue;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}

