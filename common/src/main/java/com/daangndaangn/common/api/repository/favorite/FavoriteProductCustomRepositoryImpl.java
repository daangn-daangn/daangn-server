package com.daangndaangn.common.api.repository.favorite;

import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import com.daangndaangn.common.api.entity.favorite.QFavoriteProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FavoriteProductCustomRepositoryImpl implements FavoriteProductCustomRepository {

    private static final QFavoriteProduct qFavoriteProduct = QFavoriteProduct.favoriteProduct;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<FavoriteProduct> findByProductAndUser(Long productId, Long userId) {

        FavoriteProduct favoriteProduct = jpaQueryFactory
                .select(qFavoriteProduct)
                .from(qFavoriteProduct)
                    .join(qFavoriteProduct.product).fetchJoin()
                    .join(qFavoriteProduct.user).fetchJoin()
                .where(qFavoriteProduct.product.id.eq(productId),
                        qFavoriteProduct.user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(favoriteProduct);
    }

    @Override
    public Optional<FavoriteProduct> findByIdWithUser(Long id) {
        FavoriteProduct favoriteProduct = jpaQueryFactory
                .select(qFavoriteProduct)
                .from(qFavoriteProduct)
                .join(qFavoriteProduct.user).fetchJoin()
                .where(qFavoriteProduct.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(favoriteProduct);
    }

    @Override
    public List<FavoriteProduct> findAll(Long userId, Pageable pageable) {
        return jpaQueryFactory.select(qFavoriteProduct)
                .from(qFavoriteProduct)
                    .join(qFavoriteProduct.product).fetchJoin()
                    .join(qFavoriteProduct.user).fetchJoin()
                .where(qFavoriteProduct.user.id.eq(userId))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                .fetch();
    }
}
