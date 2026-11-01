package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.ProgramSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProgramSaleRepository extends JpaRepository<ProgramSale, String> {

    List<ProgramSale> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate now1, LocalDate now2);
}

