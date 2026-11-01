package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query("SELECT e FROM Employee e JOIN FETCH e.subsidiary WHERE (:subId IS NULL OR e.subsidiary.subId = :subId)")
    Page<Employee> findBySubsidiary(@Param("subId") String subId, Pageable pageable);

    List<Employee> findByRole(String role);
}
