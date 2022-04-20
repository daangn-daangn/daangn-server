package com.daangndaangn.apiserver.repository.salereview;

import com.daangndaangn.apiserver.entity.review.QSaleReview;
import com.daangndaangn.apiserver.entity.review.SaleReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SaleReviewRepositoryImpl implements SaleReviewCustom {

    private static final QSaleReview qSaleReview = QSaleReview.saleReview;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<SaleReview> findBySaleReviewId(Long saleReviewId) {
        SaleReview saleReview = jpaQueryFactory
                .select(qSaleReview)
                .from(qSaleReview)
                    .join(qSaleReview.seller).fetchJoin()
                    .join(qSaleReview.buyer).fetchJoin()
                .where(qSaleReview.id.eq(saleReviewId))
                .fetchOne();

        return Optional.ofNullable(saleReview);
    }

    @Override
    public List<SaleReview> findAllUserReview(Long userId, Pageable pageable) {
        return jpaQueryFactory.select(qSaleReview)
                .from(qSaleReview)
                    .join(qSaleReview.seller).fetchJoin()
                    .join(qSaleReview.buyer).fetchJoin()
                .where(
                    qSaleReview.seller.id.eq(userId)
                        .or(qSaleReview.buyer.id.eq(userId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<SaleReview> findAllSellerReview(Long sellerId, Pageable pageable) {
        return jpaQueryFactory.select(qSaleReview)
                .from(qSaleReview)
                    .join(qSaleReview.seller).fetchJoin()
                    .join(qSaleReview.buyer).fetchJoin()
                .where(qSaleReview.seller.id.eq(sellerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<SaleReview> findAllBuyerReview(Long buyerId, Pageable pageable) {
        return jpaQueryFactory.select(qSaleReview)
                .from(qSaleReview)
                    .join(qSaleReview.seller).fetchJoin()
                    .join(qSaleReview.buyer).fetchJoin()
                .where(qSaleReview.buyer.id.eq(buyerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
