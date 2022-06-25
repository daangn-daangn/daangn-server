package com.daangndaangn.common.api.repository.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {
}
