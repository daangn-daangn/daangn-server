package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.apiserver.controller.salereview.SaleReviewRequest;
import com.daangndaangn.common.api.entity.review.SaleReview;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaleReviewService {

    Long create(Long sellerId, Long buyerId, String content);

    SaleReview getSaleReview(Long id);

    // 전체후기
    List<SaleReview> getUserReviews(Long userId, Pageable pageable);

    // 판매자 후기
    List<SaleReview> getSellerReviews(Long sellerId, Pageable pageable);

    // 구매자 후기
    List<SaleReview> getBuyerReviews(Long buyerId, Pageable pageable);

    boolean isSellerReviewWriter(Long reviewId, Long sellerId);

    boolean isBuyerReviewWriter(Long reviewId, Long buyerId);

    void update(Long id, String content);

    void delete(Long id);
}
