package com.daangndaangn.apiserver.entity.review;

import com.daangndaangn.apiserver.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.apiserver.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        this.content = content;
    }

    @Builder
    private SaleReview(User seller, User buyer, String content) {
        this.seller = seller;
        this.buyer = buyer;
        this.content = content;
    }
}
