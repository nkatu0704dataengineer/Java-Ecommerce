package com.ecomflow.admin.service;

import com.ecomflow.admin.dto.request.ReviewReplyRequest;
import com.ecomflow.admin.dto.response.EmployeeResponse;
import com.ecomflow.admin.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewAndStaffService {
    Page<ReviewResponse> getReviews(String sentiment, Boolean hasReply, Boolean isPositive, Boolean isNegative, Boolean isNeutral, Pageable pageable);
    ReviewResponse replyToReview(String reviewId, ReviewReplyRequest request);
    Page<EmployeeResponse> getEmployees(String subsidiaryId, Pageable pageable);
}
