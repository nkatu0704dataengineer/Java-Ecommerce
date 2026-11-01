package com.ecomflow.admin.entity;

import com.ecomflow.admin.common.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity ánh xạ bảng dbo.reviews (5.459 đánh giá khách hàng có tích hợp cờ phân tích sắc thái NLP)
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseAuditEntity {

    @Id
    @Column(name = "review_id", length = 32, nullable = false)
    private String reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @Column(name = "review_status", length = 30, nullable = false)
    private String reviewStatus;

    @Column(name = "review_score", precision = 5, scale = 2, nullable = false)
    private BigDecimal reviewScore;

    @Column(name = "review_title", length = 255)
    private String reviewTitle;

    @Column(name = "review_message", columnDefinition = "NVARCHAR(MAX)")
    private String reviewMessage;

    @Column(name = "replied_at", length = 50)
    private String repliedAt;

    // 8 Cờ Boolean phân tích sắc thái & trạng thái đánh giá
    @Column(name = "has_reply", nullable = false)
    private Boolean hasReply;

    @Column(name = "is_missing_comment", nullable = false)
    private Boolean isMissingComment;

    @Column(name = "has_title_only", nullable = false)
    private Boolean hasTitleOnly;

    @Column(name = "has_message_only", nullable = false)
    private Boolean hasMessageOnly;

    @Column(name = "is_complete_review", nullable = false)
    private Boolean isCompleteReview;

    @Column(name = "is_positive_review", nullable = false)
    private Boolean isPositiveReview;

    @Column(name = "is_neutral_review", nullable = false)
    private Boolean isNeutralReview;

    @Column(name = "is_negative_review", nullable = false)
    private Boolean isNegativeReview;

    @Column(name = "review_sentiment", length = 30)
    private String reviewSentiment;
}
