package com.daangndaangn.common.chat.document.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


/**
 * 채팅 메시지
 */
@ToString
@Getter
@NoArgsConstructor
public class ChatMessage {

    private Long senderId;
    private MessageType messageType;
    private String message;
    private LocalDateTime createdAt;

    @Builder
    private ChatMessage(Long senderId,
                        MessageType messageType,
                        String message) {

        checkArgument(senderId != null, "senderId 값은 필수입니다.");
        checkArgument(messageType != null, "messageType 값은 필수입니다.");
        checkArgument(isNotEmpty(message), "message 값은 필수입니다.");

        this.senderId = senderId;
        this.messageType = messageType;
        this.message = message;
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
}
