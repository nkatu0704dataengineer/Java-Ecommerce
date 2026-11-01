package com.ecomflow.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private String accId;
    private String cusId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String city;
    private String district;
    private String customerRank;
    private Boolean isActive;
    private Integer accountAgeDays;
    private String createdAt;
}

