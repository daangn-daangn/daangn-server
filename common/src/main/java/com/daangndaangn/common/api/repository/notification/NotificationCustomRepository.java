package com.daangndaangn.common.api.repository.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationCustomRepository {
    List<Notification> findAllByUserId(Long userId, Pageable pageable);
}
