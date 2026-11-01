package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity ánh xạ bảng dbo.carts (2.292 giỏ hàng)
 */
@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart extends BaseAuditEntity {

    @Id
    @Column(name = "cart_id", length = 32, nullable = false)
    private String cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_id", referencedColumnName = "acc_id", nullable = false)
    private Customer customer;

    @Column(name = "cart_status", length = 30, nullable = false)
    private String cartStatus;

    @Column(name = "total_items", nullable = false)
    private Integer totalItems;

    @Column(name = "total_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
}

