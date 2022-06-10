package com.daangndaangn.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.support.MessageSourceAccessor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

    private static MessageSourceAccessor messageSourceAccessor;

    public static String getMessage(String key) {
        if (ObjectUtils.isEmpty(messageSourceAccessor)) {
            throw new IllegalStateException("MessageSourceAccessor is not initialized.");
        }
        return messageSourceAccessor.getMessage(key);
    }

    public static String getMessage(String key, Object... params) {
        if (ObjectUtils.isEmpty(messageSourceAccessor)) {
            throw new IllegalStateException("MessageSourceAccessor is not initialized.");
        }
        return messageSourceAccessor.getMessage(key, params);
    }

    public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        MessageUtils.messageSourceAccessor = messageSourceAccessor;
    }
}
