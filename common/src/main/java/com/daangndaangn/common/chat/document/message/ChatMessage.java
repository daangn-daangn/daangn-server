package com.daangndaangn.common.chat.document.message;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * 채팅 메시지
 */
@ToString
@Getter
@NoArgsConstructor
@QueryEntity
public class ChatMessage {

    private Long senderId;
    private MessageType messageType;
    private String message;
    private LocalDateTime createdAt;

    @Builder
    private ChatMessage(Long senderId,
                        MessageType messageType,
                        String message) {

        this.senderId = senderId;
        this.messageType = messageType;
        this.message = message;
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
}
