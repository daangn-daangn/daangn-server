package com.daangndaangn.common.message;

import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.event.SoldOutEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 *     private Long userId;
 *
 *     private String name;
 *
 *     public UserJoinedMessage() {
 *     }
 *
 *     public UserJoinedMessage(UserJoinedEvent event) {
 *         this.userId = event.getUserId().value();
 *         this.name = event.getName();
 *     }
 *
 *     public Long getUserId() {
 *         return userId;
 *     }
 *
 *     public String getName() {
 *         return name;
 *     }
 */
@ToString
@NoArgsConstructor  // 카프카 적용을 위해 jackson이 적용되려면 기본생성자 필요
@Getter
public class SoldOutMessage {

    private Long productId;
    private String productName;
    private NotificationType notificationType;
    private List<Long> userIds;

    public static SoldOutMessage of(SoldOutEvent event, List<Long> userIds) {
        return new SoldOutMessage(event, userIds);
    }

    private SoldOutMessage(SoldOutEvent event, List<Long> userIds) {
        this.productId = event.getProductId();
        this.productName = event.getProductTitle();
        this.notificationType = event.getNotificationType();
        this.userIds = userIds;
    }
}
