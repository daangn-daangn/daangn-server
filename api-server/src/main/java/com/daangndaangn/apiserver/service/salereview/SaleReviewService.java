package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.apiserver.controller.salereview.SaleReviewRequest;
import com.daangndaangn.apiserver.controller.salereview.SaleReviewResponse;
import com.daangndaangn.apiserver.entity.review.SaleReview;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaleReviewService {

    /**
     * DTO Region (for other controllers)
     */
    Long createSaleReview(SaleReviewRequest.CreateRequest request);

    SaleReviewResponse.GetResponse getSaleReview(Long id);

    List<SaleReviewResponse.GetResponse> getAllUserReview(Long userId, Pageable pageable);

    List<SaleReviewResponse.GetResponse> getAllSellerReview(Long userId, Pageable pageable);

    List<SaleReviewResponse.GetResponse> getAllBuyerReview(Long userId, Pageable pageable);

    void updateSaleReview(Long id, SaleReviewRequest.UpdateRequest request);

    /**
     * Entity Region (for other services)
     */
    SaleReview create(Long sellerId, Long buyerId, String content);

    SaleReview findSaleReview(Long id);

    // 전체후기
    List<SaleReview> findAllUserReview(Long userId, Pageable pageable);

    // 판매자 후기
    List<SaleReview> findAllSellerReview(Long sellerId, Pageable pageable);

    // 구매자 후기
    List<SaleReview> findAllBuyerReview(Long buyerId, Pageable pageable);

    boolean isSellerReviewWriter(Long reviewId, Long sellerId);

    boolean isBuyerReviewWriter(Long reviewId, Long buyerId);

    SaleReview update(Long id, String content);

    void delete(Long id);
}
