package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity ánh xạ bảng dbo.order_items (6.464 dòng sản phẩm bán ra)
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseAuditEntity {

    @EmbeddedId
    private OrderItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subsidiary_id", referencedColumnName = "sub_id", nullable = false)
    private Subsidiary subsidiary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", referencedColumnName = "sale_id")
    private ProgramSale programSale;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "list_price", precision = 18, scale = 2, nullable = false)
    private BigDecimal listPrice;

    @Column(name = "discount_rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal discountRate;

    @Column(name = "sale_price", precision = 18, scale = 2, nullable = false)
    private BigDecimal salePrice;

    @Column(name = "total_price", precision = 18, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "item_total_weight_g")
    private Double itemTotalWeightG;

    @Column(name = "shipping_distance_km")
    private Double shippingDistanceKm;

    @Column(name = "shipping_fee", precision = 18, scale = 2, nullable = false)
    private BigDecimal shippingFee;
}

