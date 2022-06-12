package com.daangndaangn.common.api.entity.review;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sale_reviews")
public class SaleReview extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    @Column(nullable = false)
    private SaleReviewType saleReviewType;

    @Column(nullable = false, length = 500)
    private String content;

    public void update(String content) {
        checkArgument(
                isEmpty(content) || content.length() <= 500,
                "리뷰 내용은 500자 이하여야 합니다.");

        this.content = isEmpty(content) ? this.content : content;
    }

    @Builder
    private SaleReview(Product product, User reviewer, User reviewee, SaleReviewType saleReviewType, String content) {
        checkArgument(product != null, "물품 정보는 필수입니다.");
        checkArgument(reviewer != null, "리뷰 작성자 정보는 필수입니다.");
        checkArgument(reviewee != null, "리뷰 작성대상자 정보는 필수입니다.");
        checkArgument(isNotEmpty(content), "리뷰 내용은 필수입니다.");
        checkArgument(content.length() <= 500, "리뷰 내용은 500자 이하여야 합니다.");

        this.product = product;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.saleReviewType = saleReviewType;
        this.content = content;
    }
}
