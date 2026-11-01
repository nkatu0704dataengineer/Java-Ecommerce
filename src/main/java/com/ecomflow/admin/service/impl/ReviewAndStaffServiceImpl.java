package com.ecomflow.admin.service.impl;

import com.ecomflow.admin.common.exception.ResourceNotFoundException;
import com.ecomflow.admin.dto.request.ReviewReplyRequest;
import com.ecomflow.admin.dto.response.EmployeeResponse;
import com.ecomflow.admin.dto.response.ReviewResponse;
import com.ecomflow.admin.entity.Employee;
import com.ecomflow.admin.entity.Review;
import com.ecomflow.admin.repository.EmployeeRepository;
import com.ecomflow.admin.repository.ReviewRepository;
import com.ecomflow.admin.service.ReviewAndStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewAndStaffServiceImpl implements ReviewAndStaffService {

    private final ReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Page<ReviewResponse> getReviews(String sentiment, Boolean hasReply, Boolean isPositive, Boolean isNegative, Boolean isNeutral, Pageable pageable) {
        Page<Review> page = reviewRepository.filterReviews(sentiment, hasReply, isPositive, isNegative, isNeutral, pageable);
        return page.map(r -> ReviewResponse.builder()
                .reviewId(r.getReviewId())
                .orderId(r.getOrder() != null ? r.getOrder().getOrderId() : null)
                .productId(r.getProduct() != null ? r.getProduct().getProductId() : null)
                .productName(r.getProduct() != null ? r.getProduct().getProductName() : null)
                .customerName(r.getOrder() != null && r.getOrder().getCustomer() != null ? r.getOrder().getCustomer().getFullName() : null)
                .reviewScore(r.getReviewScore())
                .reviewTitle(r.getReviewTitle())
                .reviewMessage(r.getReviewMessage())
                .reviewSentiment(r.getReviewSentiment())
                .reviewStatus(r.getReviewStatus())
                .hasReply(r.getHasReply())
                .repliedAt(r.getRepliedAt())
                .isPositiveReview(r.getIsPositiveReview())
                .isNeutralReview(r.getIsNeutralReview())
                .isNegativeReview(r.getIsNegativeReview())
                .isMissingComment(r.getIsMissingComment())
                .createdAt(r.getCreatedAt())
                .build());
    }

    @Override
    @Transactional
    public ReviewResponse replyToReview(String reviewId, ReviewReplyRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá: " + reviewId));

        review.setHasReply(true);
        review.setRepliedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Review saved = reviewRepository.save(review);

        return ReviewResponse.builder()
                .reviewId(saved.getReviewId())
                .orderId(saved.getOrder() != null ? saved.getOrder().getOrderId() : null)
                .productId(saved.getProduct() != null ? saved.getProduct().getProductId() : null)
                .productName(saved.getProduct() != null ? saved.getProduct().getProductName() : null)
                .customerName(saved.getOrder() != null && saved.getOrder().getCustomer() != null ? saved.getOrder().getCustomer().getFullName() : null)
                .reviewScore(saved.getReviewScore())
                .reviewTitle(saved.getReviewTitle())
                .reviewMessage(saved.getReviewMessage())
                .reviewSentiment(saved.getReviewSentiment())
                .reviewStatus(saved.getReviewStatus())
                .hasReply(saved.getHasReply())
                .repliedAt(saved.getRepliedAt())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public Page<EmployeeResponse> getEmployees(String subsidiaryId, Pageable pageable) {
        Page<Employee> page = employeeRepository.findBySubsidiary(subsidiaryId, pageable);
        return page.map(emp -> EmployeeResponse.builder()
                .employeeId(emp.getEmployeeId())
                .fullName(emp.getFullName())
                .role(emp.getRole())
                .subsidiaryId(emp.getSubsidiary() != null ? emp.getSubsidiary().getSubId() : null)
                .subsidiaryName(emp.getSubsidiary() != null ? emp.getSubsidiary().getSubName() : null)
                .subsidiaryCity(emp.getSubsidiary() != null ? emp.getSubsidiary().getSubCity() : null)
                .email(emp.getEmail())
                .phone(emp.getPhone())
                .recruitedAt(emp.getRecruitedAt())
                .build());
    }
}

