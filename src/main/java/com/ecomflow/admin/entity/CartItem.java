package com.ecomflow.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity ánh xạ bảng dbo.cart_items (8.259 sản phẩm đang lưu trong giỏ hàng)
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @EmbeddedId
    private CartItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "list_price", precision = 18, scale = 2, nullable = false)
    private BigDecimal listPrice;

    @Column(name = "item_total_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal itemTotalAmount;

    @Column(name = "added_at", length = 50, nullable = false)
    private String addedAt;

    @Column(name = "updated_at", length = 50)
    private String updatedAt;

    @Column(name = "ingestion_time", length = 50)
    private String ingestionTime;
}
