package com.ecomflow.admin.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp trừu tượng BaseAuditEntity kế thừa bởi các Entity có quản lý dấu vết thời gian.
 * Lưu ý: Trong ecomflow_db, các trường thời gian được lưu dưới dạng chuỗi định dạng NVARCHAR(50).
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseAuditEntity {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Column(name = "created_at", length = 50)
    private String createdAt;

    @Column(name = "updated_at", length = 50)
    private String updatedAt;

    @Column(name = "ingestion_time", length = 50)
    private String ingestionTime;

    @PrePersist
    public void onPrePersist() {
        String nowStr = LocalDateTime.now().format(FORMATTER);
        if (this.createdAt == null) {
            this.createdAt = nowStr;
        }
        if (this.updatedAt == null) {
            this.updatedAt = nowStr;
        }
        if (this.ingestionTime == null) {
            this.ingestionTime = nowStr;
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now().format(FORMATTER);
    }
}

