package com.daangndaangn.common.event;

import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.api.entity.product.Product;
import lombok.Getter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 찜한 상품이 거래 완료된 경우
 */
@ToString
@Getter
public class SoldOutEvent {

    private final Long productId;
    private final String productTitle;
    private final NotificationType notificationType;

    public static SoldOutEvent from(Product product) {
        return new SoldOutEvent(product);
    }

    private SoldOutEvent(Product product) {
        checkArgument(product != null, "product 값은 필수입니다.");
        checkArgument(product.getId() != null, "product id 값은 필수입니다.");

        this.productId = product.getId();
        this.productTitle = product.getTitle();
        this.notificationType = NotificationType.SOLD_OUT;
    }
}
