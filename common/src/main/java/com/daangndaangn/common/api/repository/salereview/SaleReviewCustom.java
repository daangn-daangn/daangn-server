package com.daangndaangn.common.api.repository.salereview;

import com.daangndaangn.common.api.entity.review.SaleReview;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * SpringData Jpa Repository 기능 외에 Querydsl을 사용할 추가 기능을 정의
 */
public interface SaleReviewCustom {
    Optional<SaleReview> findBySaleReviewId(Long saleReviewId);
    List<SaleReview> findAllUserReview(Long userId, Pageable pageable);
    List<SaleReview> findAllSellerReview(Long sellerId, Pageable pageable);
    List<SaleReview> findAllBuyerReview(Long buyerId, Pageable pageable);
}
