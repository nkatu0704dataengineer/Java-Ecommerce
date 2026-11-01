package com.ecomflow.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Khóa phức hợp Composite PK cho bảng cart_items (cart_id + item_sequence)
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemId implements Serializable {

    @Column(name = "cart_id", length = 32, nullable = false)
    private String cartId;

    @Column(name = "item_sequence", nullable = false)
    private Integer itemSequence;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemId that = (CartItemId) o;
        return Objects.equals(cartId, that.cartId) && Objects.equals(itemSequence, that.itemSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, itemSequence);
    }
}

