package com.daangndaangn.apiserver.repository.salereview;

import com.daangndaangn.apiserver.entity.review.SaleReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleReviewRepository extends JpaRepository<SaleReview, Long>, SaleReviewCustom {
}
