package com.daangndaangn.common.api.repository.salereview;

import com.daangndaangn.common.api.entity.review.SaleReview;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * SpringData Jpa Repository 기능 외에 Querydsl을 사용할 추가 기능을 정의
 */
public interface SaleReviewCustomRepository {
    Optional<SaleReview> findBySaleReviewId(Long saleReviewId);

    // 특정 사용자가 받은 후기 조회. 즉, 특정 사용자와 거래한 사람들이 남긴 후기에 대한 조회
    List<SaleReview> findAllUserReview(Long userId, Pageable pageable);

    // 특정 사용자가 받은 판매자 후기 조회. 즉, 특정 사용자와 거래한 판매자가 남긴 후기에 대한 조회
    List<SaleReview> findAllSellerReview(Long userId, Pageable pageable);

    // 특정 사용자가 받은 구매자 후기 조회. 즉, 특정 사용자와 거래한 구매자가 남긴 후기에 대한 조회
    List<SaleReview> findAllBuyerReview(Long userId, Pageable pageable);

    boolean existBuyerReview(Long productId, Long userId);

    boolean existSellerReview(Long productId, Long userId);
}
