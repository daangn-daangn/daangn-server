package com.daangndaangn.common.chat.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;


/**
 * 채팅 메시지
 */
@Builder
@Getter
@Document(collection = "chatting_message")
public class ChattingMessage {
    @Id
    private String id;
    private String roomId;
    private String senderId;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
