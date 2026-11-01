package com.ecomflow.admin.repository;

import com.ecomflow.admin.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.product " +
           "JOIN FETCH r.order o " +
           "JOIN FETCH o.customer " +
           "WHERE (:sentiment IS NULL OR r.reviewSentiment = :sentiment) " +
           "AND (:hasReply IS NULL OR r.hasReply = :hasReply) " +
           "AND (:isPositive IS NULL OR r.isPositiveReview = :isPositive) " +
           "AND (:isNegative IS NULL OR r.isNegativeReview = :isNegative) " +
           "AND (:isNeutral IS NULL OR r.isNeutralReview = :isNeutral)")
    Page<Review> filterReviews(
            @Param("sentiment") String sentiment,
            @Param("hasReply") Boolean hasReply,
            @Param("isPositive") Boolean isPositive,
            @Param("isNegative") Boolean isNegative,
            @Param("isNeutral") Boolean isNeutral,
            Pageable pageable
    );

    @Query("SELECT r.reviewSentiment, COUNT(r) FROM Review r WHERE r.reviewSentiment IS NOT NULL GROUP BY r.reviewSentiment")
    List<Object[]> countBySentiment();
}

