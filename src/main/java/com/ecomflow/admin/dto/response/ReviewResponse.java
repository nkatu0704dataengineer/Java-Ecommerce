package com.ecomflow.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String reviewId;
    private String orderId;
    private String productId;
    private String productName;
    private String customerName;
    private BigDecimal reviewScore;
    private String reviewTitle;
    private String reviewMessage;
    private String reviewSentiment;
    private String reviewStatus;
    private Boolean hasReply;
    private String repliedAt;
    private Boolean isPositiveReview;
    private Boolean isNeutralReview;
    private Boolean isNegativeReview;
    private Boolean isMissingComment;
    private String createdAt;
}

