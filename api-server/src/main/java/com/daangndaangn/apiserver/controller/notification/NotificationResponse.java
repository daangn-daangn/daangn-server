package com.daangndaangn.apiserver.controller.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import static com.daangndaangn.common.api.entity.notification.NotificationConstants.*;

public class NotificationResponse {

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SimpleResponse {
        private Long id;
        private Integer notiCode;
        private String notiType;
        private Long productId;
        private Long sellerId;
        private Long saleReviewId;

        public static SimpleResponse from(Notification noti) {
            switch (noti.getNotificationType()) {
                case BUYER_REVIEW_CREATED:
                    Long reviewId = Long.valueOf(noti.getIdentifier().substring(SALE_REVIEW_PREFIX.length()));
                    return SimpleResponse.builder()
                            .id(noti.getId())
                            .notiCode(noti.getNotificationType().getCode())
                            .notiType(noti.getNotificationType().getState())
                            .saleReviewId(reviewId)
                            .build();
                case SOLD_OUT_TO_BUYER:
                    String[] component = noti.getIdentifier().split("-");
                    Long product = Long.valueOf(component[0].substring(PRODUCT_PREFIX.length()));
                    Long seller = Long.valueOf(component[1].substring(SELLER_PREFIX.length()));
                    return SimpleResponse.builder()
                            .id(noti.getId())
                            .notiCode(noti.getNotificationType().getCode())
                            .notiType(noti.getNotificationType().getState())
                            .productId(product)
                            .sellerId(seller)
                            .build();
                default:
                    Long productId = Long.valueOf(noti.getIdentifier().substring(PRODUCT_PREFIX.length()));
                    return SimpleResponse.builder()
                            .id(noti.getId())
                            .notiCode(noti.getNotificationType().getCode())
                            .notiType(noti.getNotificationType().getState())
                            .productId(productId)
                            .build();
            }
        }
    }
}
