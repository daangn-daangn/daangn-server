package com.daangndaangn.apiserver.service.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotifications(Long userId, Pageable pageable);

    Notification getNotification(Long id);

    void toRead(Long notificationId, Long userId);   //읽음 처리
}
