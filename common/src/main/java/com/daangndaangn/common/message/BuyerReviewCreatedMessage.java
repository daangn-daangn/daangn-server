package com.daangndaangn.common.message;

import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.event.BuyerReviewCreatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor  // 카프카 적용을 위해 jackson이 적용되려면 기본생성자 필요
@Getter
public class BuyerReviewCreatedMessage {

    private Long sellerId;
    private Long reviewerId;    //Buyer
    private NotificationType notificationType;

    public static BuyerReviewCreatedMessage from(BuyerReviewCreatedEvent event) {
        return new BuyerReviewCreatedMessage(event);
    }

    private BuyerReviewCreatedMessage(BuyerReviewCreatedEvent event) {
        this.sellerId = event.getSellerId();
        this.reviewerId = event.getReviewerId();
        this.notificationType = event.getNotificationType();
    }
}
