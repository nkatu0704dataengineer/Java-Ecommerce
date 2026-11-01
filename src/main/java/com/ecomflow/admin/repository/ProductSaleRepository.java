package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.ProductSale;
import com.ecomflow.admin.entity.ProductSaleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, ProductSaleId> {

    List<ProductSale> findByIdSaleId(String saleId);

    List<ProductSale> findByIdProductId(String productId);
}

