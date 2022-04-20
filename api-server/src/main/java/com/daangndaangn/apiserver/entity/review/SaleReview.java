package com.daangndaangn.apiserver.entity.review;

import com.daangndaangn.apiserver.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.apiserver.entity.user.User;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sale_reviews")
public class SaleReview extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Column(nullable = false, length = 500)
    private String content;

    public void update(String content) {
        Preconditions.checkArgument(
                StringUtils.isEmpty(content) || content.length() <= 500,
                "리뷰 내용은 500자 이하여야 합니다.");

        this.content = StringUtils.isEmpty(content) ? this.content : content;
    }

    @Builder
    private SaleReview(User seller, User buyer, String content) {
        Preconditions.checkArgument(seller != null, "판매자 정보는 필수입니다.");
        Preconditions.checkArgument(buyer != null, "구매자 정보는 필수입니다.");
        Preconditions.checkArgument(StringUtils.isNotEmpty(content), "리뷰 내용은 필수입니다.");
        Preconditions.checkArgument(content.length() <= 500, "리뷰 내용은 500자 이하여야 합니다.");

        this.seller = seller;
        this.buyer = buyer;
        this.content = content;
    }
}
