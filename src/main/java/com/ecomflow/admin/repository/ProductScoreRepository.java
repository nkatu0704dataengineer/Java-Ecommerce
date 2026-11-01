package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.ProductScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductScoreRepository extends JpaRepository<ProductScore, String> {
}
