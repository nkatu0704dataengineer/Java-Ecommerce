package com.ecomflow.admin.service.impl;

import com.ecomflow.admin.common.exception.ResourceNotFoundException;
import com.ecomflow.admin.dto.response.CartItemResponse;
import com.ecomflow.admin.dto.response.CartResponse;
import com.ecomflow.admin.dto.response.CustomerResponse;
import com.ecomflow.admin.entity.Cart;
import com.ecomflow.admin.entity.Customer;
import com.ecomflow.admin.repository.CartRepository;
import com.ecomflow.admin.repository.CustomerRepository;
import com.ecomflow.admin.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    @Override
    public Page<CustomerResponse> getCustomers(String keyword, String rank, Boolean isActive, Pageable pageable) {
        Page<Customer> page = customerRepository.searchCustomers(keyword, rank, isActive, pageable);
        return page.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public CustomerResponse toggleCustomerActive(String accId) {
        Customer customer = customerRepository.findById(accId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng có mã tài khoản: " + accId));

        customer.setIsActive(!Boolean.TRUE.equals(customer.getIsActive()));
        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    @Override
    public CartResponse getCustomerCart(String accId) {
        Customer customer = customerRepository.findById(accId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng: " + accId));

        Cart cart = cartRepository.findByCustomerAccId(accId).orElse(null);
        if (cart == null) {
            return CartResponse.builder()
                    .customerId(customer.getAccId())
                    .customerName(customer.getFullName())
                    .cartStatus("EMPTY")
                    .totalItems(0)
                    .totalAmount(BigDecimal.ZERO)
                    .items(new ArrayList<>())
                    .build();
        }

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .customerId(customer.getAccId())
                .customerName(customer.getFullName())
                .cartStatus(cart.getCartStatus())
                .totalItems(cart.getTotalItems())
                .totalAmount(cart.getTotalAmount())
                .items(cart.getItems().stream().map(ci -> CartItemResponse.builder()
                        .itemSequence(ci.getId().getItemSequence())
                        .productId(ci.getProduct() != null ? ci.getProduct().getProductId() : null)
                        .productName(ci.getProduct() != null ? ci.getProduct().getProductName() : null)
                        .quantity(ci.getQuantity())
                        .listPrice(ci.getListPrice())
                        .itemTotalAmount(ci.getItemTotalAmount())
                        .addedAt(ci.getAddedAt())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    private CustomerResponse mapToResponse(Customer c) {
        return CustomerResponse.builder()
                .accId(c.getAccId())
                .cusId(c.getCusId())
                .fullName(c.getFullName())
                .email(c.getEmail())
                .phoneNumber(c.getPhoneNumber())
                .city(c.getCity())
                .district(c.getDistrict())
                .customerRank(c.getCustomerRank())
                .isActive(c.getIsActive())
                .accountAgeDays(c.getAccountAgeDays())
                .createdAt(c.getCreatedAt())
                .build();
    }
}

