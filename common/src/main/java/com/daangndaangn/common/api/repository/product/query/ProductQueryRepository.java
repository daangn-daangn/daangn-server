package com.daangndaangn.common.api.repository.product.query;

import com.daangndaangn.common.api.entity.user.Location;
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
                    addressContains(address)
                )
                .groupBy(product.id)
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
}
