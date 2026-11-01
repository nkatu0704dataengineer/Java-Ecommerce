package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.CartItem;
import com.ecomflow.admin.entity.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {

    List<CartItem> findByIdCartId(String cartId);
}

