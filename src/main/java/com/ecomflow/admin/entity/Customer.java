package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity ánh xạ bảng dbo.customers (2.402 khách hàng đăng ký tài khoản)
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseAuditEntity {

    @Id
    @Column(name = "acc_id", length = 32, nullable = false)
    private String accId;

    @Column(name = "cus_id", length = 32, nullable = false)
    private String cusId;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "birth_date", length = 50)
    private String birthDate;

    @Column(name = "age")
    private Integer age;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "account_name", length = 50, nullable = false)
    private String accountName;

    @Column(name = "customer_rank", length = 30, nullable = false)
    private String customerRank;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "account_age_days")
    private Integer accountAgeDays;

    public String getFullName() {
        return lastName + " " + firstName;
    }
}

