package com.daangndaangn.common.event;

import com.daangndaangn.common.api.entity.product.Product;
import lombok.Getter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 특정 상품을 조회한 경우
 */
@ToString
@Getter
public class ProductViewEvent {

    private final Long productId;

    public static ProductViewEvent from(Product product) {
        return new ProductViewEvent(product);
    }

    private ProductViewEvent(Product product) {
        checkArgument(product != null, "product 값은 필수입니다.");
        checkArgument(product.getId() != null, "product id 값은 필수입니다.");

        this.productId = product.getId();
    }
}
