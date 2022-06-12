package com.daangndaangn.common.api.repository.salereview;

import com.daangndaangn.common.api.entity.review.QSaleReview;
import com.daangndaangn.common.api.entity.review.SaleReview;
import com.daangndaangn.common.api.entity.review.SaleReviewType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SaleReviewCustomRepositoryImpl implements SaleReviewCustomRepository {

    private static final QSaleReview qSaleReview = QSaleReview.saleReview;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<SaleReview> findBySaleReviewId(Long saleReviewId) {
        SaleReview saleReview = jpaQueryFactory
                .select(qSaleReview)
                .from(qSaleReview)
                    .join(qSaleReview.product).fetchJoin()
                    .join(qSaleReview.reviewer).fetchJoin()
                    .join(qSaleReview.reviewee).fetchJoin()
                .where(qSaleReview.id.eq(saleReviewId))
                .fetchOne();

        return Optional.ofNullable(saleReview);
    }

    /**
     * 특정 사용자가 받은 후기 조회
     * 즉, 특정 사용자와 거래한 사람들이 남긴 후기에 대한 조회
     */
    @Override
    public List<SaleReview> findAllUserReview(Long userId, Pageable pageable) {
        return jpaQueryFactory.select(qSaleReview)
                .from(qSaleReview)
                    .join(qSaleReview.product).fetchJoin()
                    .join(qSaleReview.reviewer).fetchJoin()
                    .join(qSaleReview.reviewee).fetchJoin()
                .where(qSaleReview.reviewee.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 특정 사용자가 받은 판매자 후기 조회
     * 즉, 특정 사용자와 거래한 판매자가 남긴 후기에 대한 조회
     */
    @Override
    public List<SaleReview> findAllSellerReview(Long userId, Pageable pageable) {
        return jpaQueryFactory.select(qSaleReview)
                .from(qSaleReview)
                    .join(qSaleReview.product).fetchJoin()
                    .join(qSaleReview.reviewer).fetchJoin()
                    .join(qSaleReview.reviewee).fetchJoin()
                .where(qSaleReview.reviewee.id.eq(userId),
                       qSaleReview.saleReviewType.eq(SaleReviewType.SELLER_REVIEW)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 특정 사용자가 받은 구매자 후기 조회
     * 즉, 특정 사용자와 거래한 구매자가 남긴 후기에 대한 조회
     */
    @Override
    public List<SaleReview> findAllBuyerReview(Long userId, Pageable pageable) {
        return jpaQueryFactory.select(qSaleReview)
                .from(qSaleReview)
                .join(qSaleReview.product).fetchJoin()
                .join(qSaleReview.reviewer).fetchJoin()
                .join(qSaleReview.reviewee).fetchJoin()
                .where(qSaleReview.reviewee.id.eq(userId),
                        qSaleReview.saleReviewType.eq(SaleReviewType.BUYER_REVIEW)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public boolean existBuyerReview(Long productId, Long userId) {
        List<SaleReview> result = jpaQueryFactory.select(qSaleReview)
                .from(qSaleReview)
                .join(qSaleReview.product).fetchJoin()
                .join(qSaleReview.reviewer).fetchJoin()
                .join(qSaleReview.reviewee).fetchJoin()
                .where(qSaleReview.product.id.eq(productId),
                        qSaleReview.reviewer.id.eq(userId),
                        qSaleReview.saleReviewType.eq(SaleReviewType.BUYER_REVIEW)
                )
                .fetch();

        return !result.isEmpty();
    }
}
