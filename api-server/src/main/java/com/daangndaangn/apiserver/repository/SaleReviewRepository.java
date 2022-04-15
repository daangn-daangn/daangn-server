package com.daangndaangn.apiserver.repository;

import com.daangndaangn.apiserver.entity.review.SaleReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SaleReviewRepository extends JpaRepository<SaleReview, Long> {

    @Query("select sr from SaleReview sr " +
                        "join fetch sr.buyer buyer " +
                        "join fetch sr.seller seller " +
                    "where sr.id = :id")
    Optional<SaleReview> findById(@Param("id") Long id);

    // 전체후기
    @Query("select sr from SaleReview sr " +
                        "join fetch sr.buyer buyer " +
                        "join fetch sr.seller seller " +
                    "where seller.id = :userId " +
                        "or buyer.id = :userId")
    List<SaleReview> findAllUserReview(@Param("userId") Long userId, Pageable pageable);

    // 판매자 후기
    @Query("select sr from SaleReview sr " +
                        "join fetch sr.buyer buyer " +
                        "join fetch sr.seller seller " +
                    "where seller.id = :sellerId")
    List<SaleReview> findAllSellerReview(@Param("sellerId") Long sellerId, Pageable pageable);

    // 구매자 후기
    @Query("select sr from SaleReview sr " +
                        "join fetch sr.buyer buyer " +
                        "join fetch sr.seller seller " +
                    "where buyer.id = :buyerId")
    List<SaleReview> findAllBuyerReview(@Param("buyerId") Long buyerId, Pageable pageable);
}
