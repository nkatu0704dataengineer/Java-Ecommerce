package com.ecomflow.admin.controller;

import com.ecomflow.admin.common.ApiResponse;
import com.ecomflow.admin.dto.request.ReviewReplyRequest;
import com.ecomflow.admin.dto.response.EmployeeResponse;
import com.ecomflow.admin.dto.response.ReviewResponse;
import com.ecomflow.admin.service.ReviewAndStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller kiểm duyệt Đánh giá khách hàng & Danh bạ nhân sự chi nhánh
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewAndStaffController {

    private final ReviewAndStaffService reviewAndStaffService;

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getReviews(
            @RequestParam(required = false) String sentiment,
            @RequestParam(required = false) Boolean hasReply,
            @RequestParam(required = false) Boolean isPositive,
            @RequestParam(required = false) Boolean isNegative,
            @RequestParam(required = false) Boolean isNeutral,
            @PageableDefault(size = 20, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReviewResponse> page = reviewAndStaffService.getReviews(sentiment, hasReply, isPositive, isNegative, isNeutral, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách đánh giá khách hàng thành công", page));
    }

    @PostMapping("/reviews/{id}/reply")
    public ResponseEntity<ApiResponse<ReviewResponse>> replyToReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewReplyRequest request
    ) {
        ReviewResponse response = reviewAndStaffService.replyToReview(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Gửi phản hồi CSKH thành công", response));
    }

    @GetMapping("/employees")
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> getEmployees(
            @RequestParam(required = false) String subsidiaryId,
            @PageableDefault(size = 20, sort = "employeeId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<EmployeeResponse> page = reviewAndStaffService.getEmployees(subsidiaryId, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh bạ nhân sự chi nhánh thành công", page));
    }
}
