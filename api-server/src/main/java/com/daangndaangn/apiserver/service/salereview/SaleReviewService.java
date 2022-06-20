package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.review.SaleReview;
import com.daangndaangn.common.api.entity.review.SaleReviewType;
import com.daangndaangn.common.api.entity.user.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SaleReviewService {

    CompletableFuture<Long> create(Long productId, Long reviewerId, Long revieweeId, SaleReviewType saleReviewType, String content);

    boolean isValidCreateRequest(Product product, User reviewer, User reviewee, SaleReviewType saleReviewTypeCode);

    SaleReview getSaleReview(Long id);

    // 전체후기
    List<SaleReview> getUserReviews(Long userId, Pageable pageable);

    // 판매자 후기
    List<SaleReview> getSellerReviews(Long userId, Pageable pageable);

    // 구매자 후기
    List<SaleReview> getBuyerReviews(Long userId, Pageable pageable);

    boolean isSellerReviewWriter(Long saleReviewId, Long userId);

    boolean isBuyerReviewWriter(Long saleReviewId, Long userId);

    boolean existBuyerReview(Long productId, Long userId);

    boolean existSellerReview(Long productId, Long userId);

    void update(Long id, String content);

    void hide(Long id, Long userId);

    void delete(Long id);
}
