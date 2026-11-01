package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.SubProduct;
import com.ecomflow.admin.entity.SubProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubProductRepository extends JpaRepository<SubProduct, SubProductId> {

    @Query("SELECT COUNT(sp) FROM SubProduct sp WHERE sp.quantity < :threshold")
    long countLowStock(@Param("threshold") int threshold);

    @Query("SELECT sp FROM SubProduct sp " +
           "JOIN FETCH sp.subsidiary " +
           "JOIN FETCH sp.product p " +
           "WHERE (:subId IS NULL OR sp.id.subsidiaryId = :subId) " +
           "AND (:keyword IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(sp.id.productId) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:isLowStock IS NULL OR (:isLowStock = true AND sp.quantity < 5) OR (:isLowStock = false AND sp.quantity >= 5))")
    Page<SubProduct> findInventory(
            @Param("subId") String subId,
            @Param("keyword") String keyword,
            @Param("isLowStock") Boolean isLowStock,
            Pageable pageable
    );

    @Query("SELECT sp FROM SubProduct sp " +
           "JOIN FETCH sp.subsidiary " +
           "JOIN FETCH sp.product " +
           "WHERE sp.quantity < 5")
    List<SubProduct> findTopLowStock(Pageable pageable);
}

