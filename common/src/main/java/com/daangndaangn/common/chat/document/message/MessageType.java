package com.daangndaangn.common.chat.document.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Getter
@AllArgsConstructor
public enum MessageType {
    MESSAGE(1, "일반 메시지"),
    POSITION(2, "좌표"),
    IMAGE(3, "이미지 URL"),
    EXIT(4, "채팅방 퇴장");

    private static final Map<Integer, MessageType> messageTypeMap =
            Stream.of(values()).collect(toMap(MessageType::getCode, value -> value));

    private Integer code;
    private String state;

    public static MessageType from(int messageTypeCode) {
        MessageType messageType = messageTypeMap.get(messageTypeCode);
        if (isEmpty(messageType)) {
            throw new IllegalArgumentException("잘못된 messageType 타입입니다. 1~4 중 하나를 입력해야 합니다.");
        }

        return messageType;
    }
}
