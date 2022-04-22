package com.daangndaangn.apiserver.entity.favorite;

import com.daangndaangn.apiserver.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.apiserver.entity.product.Product;
import com.daangndaangn.apiserver.entity.user.User;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "favorite_products")
public class FavoriteProduct extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private boolean isValid;

    public boolean isNotValid() {
        return !isValid;
    }

    public void update(boolean isValid) {
        this.isValid = isValid;
    }

    @Builder
    private FavoriteProduct(User user, Product product) {
        Preconditions.checkArgument(user != null, "사용자 정보는 필수입니다.");
        Preconditions.checkArgument(product != null, "물품 정보는 필수입니다.");

        this.user = user;
        this.product = product;
        this.isValid = true;
    }
}
