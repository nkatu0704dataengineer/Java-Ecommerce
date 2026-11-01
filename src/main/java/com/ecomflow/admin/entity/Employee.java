package com.ecomflow.admin.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity ánh xạ bảng dbo.employees (409 nhân viên trực thuộc các chi nhánh)
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @Column(name = "employee_id", length = 32, nullable = false)
    private String employeeId;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "born", length = 50)
    private String born;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "recruited_at", length = 50, nullable = false)
    private String recruitedAt;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "role", length = 50, nullable = false)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subsidiary_id", referencedColumnName = "sub_id", nullable = false)
    private Subsidiary subsidiary;

    @Column(name = "ingestion_time", length = 50)
    private String ingestionTime;

    public String getFullName() {
        return lastName + " " + firstName;
    }
}
