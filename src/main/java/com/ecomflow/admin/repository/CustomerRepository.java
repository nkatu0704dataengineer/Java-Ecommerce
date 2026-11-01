package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    long countByIsActiveTrue();

    @Query("SELECT c FROM Customer c " +
           "WHERE (:keyword IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.accId) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:rank IS NULL OR c.customerRank = :rank) " +
           "AND (:isActive IS NULL OR c.isActive = :isActive)")
    Page<Customer> searchCustomers(
            @Param("keyword") String keyword,
            @Param("rank") String rank,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
}

