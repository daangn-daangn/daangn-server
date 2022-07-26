package com.daangndaangn.apiserver.service.notification.query;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationQueryDto {
    private Long id;
    private NotificationType notiType;
    private Long productId;
    private Long price;
    private String title;
    private String thumbNailImage;
    private Long userId;
    private String nickname;
    private String profileUrl;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationQueryDto of(Product product, User user, Notification noti) {
        return NotificationQueryDto.builder()
                .id(noti.getId())
                .notiType(noti.getNotificationType())
                .productId(product.getId())
                .price(product.getPrice())
                .title(product.getTitle())
                .thumbNailImage(product.getThumbNailImage())
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .isRead(noti.isRead())
                .createdAt(noti.getCreatedAt())
                .build();
    }

    public static NotificationQueryDto from(Product product, Notification noti) {
        return NotificationQueryDto.builder()
                .id(noti.getId())
                .notiType(noti.getNotificationType())
                .productId(product.getId())
                .price(product.getPrice())
                .title(product.getTitle())
                .thumbNailImage(product.getThumbNailImage())
                .isRead(noti.isRead())
                .createdAt(noti.getCreatedAt())
                .build();
    }

    public static NotificationQueryDto from(User user, Notification noti) {
        return NotificationQueryDto.builder()
                .id(noti.getId())
                .notiType(noti.getNotificationType())
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .isRead(noti.isRead())
                .createdAt(noti.getCreatedAt())
                .build();
    }
}
