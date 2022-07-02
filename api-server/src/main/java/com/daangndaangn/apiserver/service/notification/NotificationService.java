package com.daangndaangn.apiserver.service.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.daangndaangn.common.api.entity.notification.NotificationConstants;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotifications(Long userId, Pageable pageable);

    Notification getNotification(Long id);

    void toRead(Long notificationId, Long userId);   //읽음 처리

    //SOLD_OUT, PRICE_DOWN
    Long getProductId(String identifier);

    //SOLD_OUT_TO_BUYER
    Long getProductIdOfSoldOutToBuyer(String identifier);

    //BUYER_REVIEW_CREATED
    Long getReviewerId(String identifier);

    void delete(Long notificationId, Long userId);
}
