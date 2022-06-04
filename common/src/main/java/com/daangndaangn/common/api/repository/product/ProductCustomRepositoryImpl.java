package com.daangndaangn.common.api.repository.product;

import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.QProductImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.daangndaangn.common.api.entity.category.QCategory.category;
import static com.daangndaangn.common.api.entity.product.QProduct.product;
import static com.daangndaangn.common.api.entity.product.QProductImage.productImage;
import static com.daangndaangn.common.api.entity.user.QUser.user;

@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Product> findByProductId(Long productId) {

        Product result = jpaQueryFactory
                .selectFrom(product)
                    .join(product.category, category).fetchJoin()
                    .join(product.seller, user).fetchJoin()
                    .leftJoin(product.buyer, user).fetchJoin()
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Product> findByProductIdWithProductImages(Long productId) {

        Product result = jpaQueryFactory
                .selectFrom(product)
                .join(product.category, category).fetchJoin()
                .join(product.seller, user).fetchJoin()
                .leftJoin(product.productImages, productImage).fetchJoin()
                .leftJoin(product.buyer, user).fetchJoin()
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Product> findByProductIdWithOnlySeller(Long productId) {

        Product result = jpaQueryFactory
                .selectFrom(product)
                    .join(product.category, category).fetchJoin()
                    .join(product.seller, user).fetchJoin()
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
