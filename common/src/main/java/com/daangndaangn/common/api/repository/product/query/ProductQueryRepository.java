package com.daangndaangn.common.api.repository.product.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.daangndaangn.common.api.entity.category.QCategory.category;
import static com.daangndaangn.common.api.entity.favorite.QFavoriteProduct.favoriteProduct;
import static com.daangndaangn.common.api.entity.product.QProduct.product;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

@RequiredArgsConstructor
@Component
public class ProductQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 특정 사용자가 찜한 product list 조회
     */
    public List<ProductQueryDto> findAll(List<Long> productIds) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(ProductQueryDto.class,
                                product.id,
                                product.title,
                                product.location.address,
                                product.price,
                                product.thumbNailImage,
                                product.createdAt,
                                favoriteProduct.id.count()
                        )
                ).from(product)
                .join(product.category, category)
                .leftJoin(favoriteProduct)
                    .on(product.id.eq(favoriteProduct.product.id))
                    .on(favoriteProduct.isValid.eq(true))
                .where(product.id.in(productIds))
                .groupBy(product.id)
                .orderBy(product.id.desc())
                .fetch();
    }

    /**
     *  private String title;
     *  private String location;
     *  private Long price;
     *  private String imageUrl;
     *  private Long favoriteCount;
     *  private LocalDateTime createdAt;
     */
    public List<ProductQueryDto> findAll(ProductSearchOption productSearchOption, String address, Pageable pageable) {

        return jpaQueryFactory
                .select(
                    Projections.constructor(ProductQueryDto.class,
                        product.id,
                        product.title,
                        product.location.address,
                        product.price,
                        product.thumbNailImage,
                        product.createdAt,
                        favoriteProduct.id.count()
                    )
                ).from(product)
                    .join(product.category, category)
                    .leftJoin(favoriteProduct).on(product.id.eq(favoriteProduct.product.id))
                                            .on(favoriteProduct.isValid.eq(true))
                .where(
                    categoriesEq(productSearchOption.getCategories()),
                    titleContains(productSearchOption.getTitle()),
                    addressContains(address),
                    priceCondition(productSearchOption.getMinPrice(), productSearchOption.getMaxPrice())
                )
                .groupBy(product.id, product.title)
                .orderBy(product.id.desc())
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                .fetch();
    }

    private BooleanExpression categoriesEq(List<Long> categories) {
        if (isEmpty(categories)) {
            return null;
        }
        return product.category.id.in(categories);
    }

    private BooleanExpression titleContains(String title) {
        if (isBlank(title)) {
            return null;
        }
        return product.title.contains(title);
    }

    private BooleanExpression addressContains(String address) {
        if (isBlank(address)) {
            return null;
        }
        return product.location.address.contains(address);
    }

    private BooleanExpression priceCondition(long minPrice, long maxPrice) {
        final long UN_USED_VALUE = -1L;

        if (minPrice == UN_USED_VALUE && maxPrice == UN_USED_VALUE) {
            return null;
        } else if (minPrice == UN_USED_VALUE) {
            return product.price.loe(maxPrice);
        } else if (maxPrice == UN_USED_VALUE) {
            return product.price.goe(minPrice);
        }

        return product.price.between(minPrice, maxPrice);
    }
}
