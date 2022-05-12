package com.daangndaangn.common.api.repository.salereview;

import com.daangndaangn.common.api.entity.review.SaleReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleReviewRepository extends JpaRepository<SaleReview, Long>, SaleReviewCustom {
}
