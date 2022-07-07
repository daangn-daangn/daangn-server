package com.daangndaangn.common.message;

import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.event.SoldOutToBuyerEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor  // 카프카 적용을 위해 jackson이 적용되려면 기본생성자 필요
@Getter
public class SoldOutToBuyerMessage {

    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private String productName;
    private NotificationType notificationType;

    public static SoldOutToBuyerMessage from(SoldOutToBuyerEvent event) {
        return new SoldOutToBuyerMessage(event);
    }

    private SoldOutToBuyerMessage(SoldOutToBuyerEvent event) {
        this.productId = event.getProductId();
        this.sellerId = event.getSellerId();
        this.buyerId = event.getBuyerId();
        this.productName = event.getProductTitle();
        this.notificationType = event.getNotificationType();
    }
}
