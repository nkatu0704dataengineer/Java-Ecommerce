package com.ecomflow.admin.service;

import com.ecomflow.admin.dto.response.CartResponse;
import com.ecomflow.admin.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomerResponse> getCustomers(String keyword, String rank, Boolean isActive, Pageable pageable);
    CustomerResponse toggleCustomerActive(String accId);
    CartResponse getCustomerCart(String accId);
}

