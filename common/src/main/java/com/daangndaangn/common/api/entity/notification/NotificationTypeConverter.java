package com.daangndaangn.common.api.entity.notification;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class NotificationTypeConverter implements AttributeConverter<NotificationType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(NotificationType notificationType) {
        return notificationType.getCode();
    }

    @Override
    public NotificationType convertToEntityAttribute(Integer notificationTypeCode) {
        return NotificationType.from(notificationTypeCode);
    }
}
