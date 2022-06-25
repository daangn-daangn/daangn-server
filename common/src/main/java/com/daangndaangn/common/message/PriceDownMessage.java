package com.daangndaangn.common.message;

import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.event.PriceDownEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor  // 카프카 적용을 위해 jackson이 적용되려면 기본생성자 필요
@Getter
public class PriceDownMessage {

    private Long productId;
    private String productName;
    private NotificationType notificationType;
    private List<Long> userIds;

    public static PriceDownMessage of(PriceDownEvent event, List<Long> userIds) {
        return new PriceDownMessage(event, userIds);
    }

    private PriceDownMessage(PriceDownEvent event, List<Long> userIds) {
        this.productId = event.getProductId();
        this.productName = event.getProductName();
        this.notificationType = event.getNotificationType();
        this.userIds = userIds;
    }
}
