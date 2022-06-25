package com.daangndaangn.notiserver.service;

import com.daangndaangn.common.api.entity.notification.NotificationType;

import java.util.List;

public interface NotificationService {
    /**
     * 구매자가 구매후기를 남긴 경우 => 판매자에게 구매 후기가 왔다는 알림 필요 => 거래 후기 조회 페이지로 이동
     */
    Long createBuyerReviewCreatedNotification(Long sellerId, NotificationType notificationType, Long saleReviewId);

    /**
     * 판매자가 판매완료로 상태를 변경한 경우 => 구매자에게 구매 후기 남기라는 알림 필요 => 거래 후기 남기는 페이지로 이동
     */
    Long createSoldOutToBuyerNotification(Long buyerId, NotificationType notificationType, Long sellerId, Long productId);

    /**
     * 찜한 상품이 거래 완료된 경우 => 내가 찜한 상품으로 이동
     * 찜한 상품의 가격이 낮아진 경우 => 내가 찜한 상품으로 이동
     */
    List<Long> createFavoriteProductNotification(List<Long> userIds, NotificationType notificationType, Long productId);
}
