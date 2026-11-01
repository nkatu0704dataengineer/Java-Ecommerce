package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity ánh xạ bảng dbo.subsidiaries (51 chi nhánh/showroom trên toàn quốc)
 */
@Entity
@Table(name = "subsidiaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subsidiary extends BaseAuditEntity {

    @Id
    @Column(name = "sub_id", length = 32, nullable = false)
    private String subId;

    @Column(name = "sub_name", length = 150, nullable = false)
    private String subName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "establishment_date", length = 50)
    private String establishmentDate;

    @Column(name = "sub_street", length = 255)
    private String subStreet;

    @Column(name = "sub_district", length = 100)
    private String subDistrict;

    @Column(name = "sub_city", length = 100, nullable = false)
    private String subCity;

    /**
     * Phương thức helper trả về địa chỉ đầy đủ của chi nhánh
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (subStreet != null && !subStreet.isBlank()) sb.append(subStreet);
        if (subDistrict != null && !subDistrict.isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(subDistrict);
        }
        if (subCity != null && !subCity.isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(subCity);
        }
        return sb.toString();
    }
}

