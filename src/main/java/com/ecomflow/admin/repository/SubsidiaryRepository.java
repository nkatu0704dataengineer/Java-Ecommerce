package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsidiaryRepository extends JpaRepository<Subsidiary, String> {
}
