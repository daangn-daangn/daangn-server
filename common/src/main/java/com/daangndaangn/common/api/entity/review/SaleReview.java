package com.daangndaangn.common.api.entity.review;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.common.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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
        checkArgument(
                isEmpty(content) || content.length() <= 500,
                "리뷰 내용은 500자 이하여야 합니다.");

        this.content = isEmpty(content) ? this.content : content;
    }

    @Builder
    private SaleReview(User seller, User buyer, String content) {
        checkArgument(seller != null, "판매자 정보는 필수입니다.");
        checkArgument(buyer != null, "구매자 정보는 필수입니다.");
        checkArgument(StringUtils.isNotEmpty(content), "리뷰 내용은 필수입니다.");
        checkArgument(content.length() <= 500, "리뷰 내용은 500자 이하여야 합니다.");

        this.seller = seller;
        this.buyer = buyer;
        this.content = content;
    }
}
