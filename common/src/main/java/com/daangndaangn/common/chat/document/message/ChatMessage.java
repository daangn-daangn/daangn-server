package com.daangndaangn.common.chat.document.message;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;


/**
 * 채팅 메시지
 */
@Getter
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;
    private String roomId;
    private Long senderId;
    private MessageType messageType;
    private String message;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    private ChatMessage(String id,
                        String roomId,
                        Long senderId,
                        MessageType messageType,
                        String message) {

        this.id = id;
        this.roomId = roomId;
        this.senderId = senderId;
        this.messageType = messageType;
        this.message = message;
    }
}
