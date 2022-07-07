package com.daangndaangn.common.event;

import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import lombok.Getter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 판매자가 판매완료로 상태를 변경한 경우,
 * 구매자에게 해당 사실을 알리기 위해 네이밍이 SoldOutToBuyerEvent
 */
@ToString
@Getter
public class SoldOutToBuyerEvent {

    private final Long productId;
    private final Long sellerId;
    private final Long buyerId;
    private final String productTitle;
    private final NotificationType notificationType;

    public static SoldOutToBuyerEvent of(Product product, User buyer) {
        return new SoldOutToBuyerEvent(product, buyer);
    }

    private SoldOutToBuyerEvent(Product product, User buyer) {
        checkArgument(product != null, "product 값은 필수입니다.");
        checkArgument(buyer != null, "buyer 값은 필수입니다");
        checkArgument(product.getId() != null, "product id 값은 필수입니다.");
        checkArgument(buyer.getId() != null, "buyer id 값은 필수입니다.");

        productId = product.getId();
        sellerId = product.getSeller().getId();
        buyerId = buyer.getId();
        productTitle = product.getTitle();
        notificationType = NotificationType.SOLD_OUT_TO_BUYER;
    }
}
