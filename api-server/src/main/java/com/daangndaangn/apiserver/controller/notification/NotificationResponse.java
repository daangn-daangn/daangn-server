package com.daangndaangn.apiserver.controller.notification;

import com.daangndaangn.apiserver.service.notification.query.NotificationQueryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class NotificationResponse {

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SimpleResponse {
        private Long id;
        private Integer notiCode;
        private String notiType;
        private Long productId;
        private Long price;
        private String title;
        private String thumbNailImage;
        private Long reviewerId;
        private String nickname;
        private String profileUrl;
        private boolean isView;
        private LocalDateTime createdAt;

        public static SimpleResponse fromUserNotice(NotificationQueryDto noti, String presignedUrl) {
            return SimpleResponse.builder()
                    .id(noti.getId())
                    .notiCode(noti.getNotiType().getCode())
                    .notiType(noti.getNotiType().getState())
                    .reviewerId(noti.getUserId())
                    .nickname(noti.getNickname())
                    .profileUrl(presignedUrl)
                    .isView(noti.isRead())
                    .createdAt(noti.getCreatedAt())
                    .build();
        }

        public static SimpleResponse fromProductNotice(NotificationQueryDto noti, String presignedUrl) {
            switch (noti.getNotiType()) {
                case SOLD_OUT:  //0
                case SOLD_OUT_TO_BUYER://2
                    return SimpleResponse.builder()
                            .id(noti.getId())
                            .notiCode(noti.getNotiType().getCode())
                            .notiType(noti.getNotiType().getState())
                            .productId(noti.getProductId())
                            .title(noti.getTitle())
                            .thumbNailImage(presignedUrl)
                            .isView(noti.isRead())
                            .createdAt(noti.getCreatedAt())
                            .build();
                case PRICE_DOWN:    //1
                    return SimpleResponse.builder()
                            .id(noti.getId())
                            .notiCode(noti.getNotiType().getCode())
                            .notiType(noti.getNotiType().getState())
                            .productId(noti.getProductId())
                            .price(noti.getPrice())
                            .title(noti.getTitle())
                            .thumbNailImage(presignedUrl)
                            .isView(noti.isRead())
                            .createdAt(noti.getCreatedAt())
                            .build();
                default:
                    throw new IllegalArgumentException("올바르지 않은 요청입니다. Notification Type을 확인하세요.");
            }
        }
    }
}
