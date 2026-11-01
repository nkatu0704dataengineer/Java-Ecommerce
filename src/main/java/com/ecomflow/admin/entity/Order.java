package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity ánh xạ bảng dbo.orders (3.212 đơn hàng)
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseAuditEntity {

    @Id
    @Column(name = "order_id", length = 32, nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_id", referencedColumnName = "acc_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @Column(name = "order_status", length = 30, nullable = false)
    private String orderStatus;

    @Column(name = "total_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "total_shipfee", precision = 18, scale = 2, nullable = false)
    private BigDecimal totalShipFee;

    @Column(name = "target_street", length = 255)
    private String targetStreet;

    @Column(name = "target_district", length = 100)
    private String targetDistrict;

    @Column(name = "target_city", length = 100, nullable = false)
    private String targetCity;

    @Column(name = "confirmed_at", length = 50)
    private String confirmedAt;

    @Column(name = "shipped_at", length = 50)
    private String shippedAt;

    @Column(name = "estimated_delivery_date", length = 50)
    private String estimatedDeliveryDate;

    @Column(name = "delivered_at", length = 50)
    private String deliveredAt;

    @Column(name = "cancel_date", length = 50)
    private String cancelDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    public String getFullTargetAddress() {
        StringBuilder sb = new StringBuilder();
        if (targetStreet != null && !targetStreet.isBlank()) sb.append(targetStreet);
        if (targetDistrict != null && !targetDistrict.isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(targetDistrict);
        }
        if (targetCity != null && !targetCity.isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(targetCity);
        }
        return sb.toString();
    }
}

