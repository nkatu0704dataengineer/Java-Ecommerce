package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity ánh xạ bảng dbo.payments (3.212 giao dịch đối soát tài chính 1:1 với orders)
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseAuditEntity {

    @Id
    @Column(name = "payment_id", length = 32, nullable = false)
    private String paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Order order;

    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod;

    @Column(name = "payment_status", length = 30, nullable = false)
    private String paymentStatus;

    @Column(name = "payment_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "paid_at", length = 50)
    private String paidAt;

    @Column(name = "refund_at", length = 50)
    private String refundAt;
}
