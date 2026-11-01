package com.ecomflow.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Khóa phức hợp Composite PK cho bảng order_items (order_id + order_item_id)
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemId implements Serializable {

    @Column(name = "order_id", length = 32, nullable = false)
    private String orderId;

    @Column(name = "order_item_id", nullable = false)
    private Integer orderItemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemId that = (OrderItemId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(orderItemId, that.orderItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderItemId);
    }
}

