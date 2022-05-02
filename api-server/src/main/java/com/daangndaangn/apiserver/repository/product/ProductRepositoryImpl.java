package com.daangndaangn.apiserver.repository.product;

import com.daangndaangn.apiserver.entity.product.Product;
import com.daangndaangn.apiserver.entity.product.ProductState;
import com.daangndaangn.apiserver.entity.product.QProduct;
import com.daangndaangn.apiserver.entity.user.Location;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductCustom{

    private static final QProduct qProduct = QProduct.product;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Product> findAllProductByLocation(Location location, Pageable pageable) {

        return jpaQueryFactory.select(qProduct)
                .from(qProduct)
                .join(qProduct.category).fetchJoin()
                .where(
                        qProduct.location.eq(location)
                                .and(qProduct.state.eq(ProductState.FOR_SALE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Product> findAllProductByFilter(String keyword, Long minPrice, Long maxPrice, Long categoryId, Location location, Pageable pageable) {
        return jpaQueryFactory.select(qProduct)
                .from(qProduct)
                .join(qProduct.category).fetchJoin()
                .where(
                        this.eqKeyword(keyword),
                        this.eqCategory(categoryId),
                        this.rangePrice(minPrice, maxPrice),
                        qProduct.location.eq(location)
                        .and(qProduct.state.eq(ProductState.FOR_SALE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression eqKeyword(String keyword){
        if(StringUtils.isEmpty(keyword)){
            return null;
        }
        return qProduct.title.contains(keyword);
    }

    private BooleanExpression rangePrice(Long minPrice, Long maxPrice){
        if(minPrice == null && maxPrice == null) return null;
        if(minPrice != null && maxPrice == null) return qProduct.price.goe(minPrice);
        return qProduct.price.between(minPrice, maxPrice);
    }

    private BooleanExpression eqCategory(Long categoryId){
        if(categoryId == null){
            return null;
        }
        return qProduct.category.id.eq(categoryId);
    }
}