package com.daangndaangn.common.event;

import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.api.entity.product.Product;
import lombok.Getter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 찜한 상품의 가격이 낮아진 경우
 */
@ToString
@Getter
public class PriceDownEvent {

    private final Long productId;
    private final String productName;
    private final NotificationType notificationType;

    public static PriceDownEvent from(Product product) {
        return new PriceDownEvent(product);
    }

    private PriceDownEvent(Product product) {
        checkArgument(product != null, "product 값은 필수입니다.");
        checkArgument(product.getId() != null, "product id 값은 필수입니다.");

        this.productId = product.getId();
        this.productName = product.getName();
        this.notificationType = NotificationType.SOLD_OUT;
    }
}
