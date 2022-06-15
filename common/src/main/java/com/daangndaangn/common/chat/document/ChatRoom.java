package com.daangndaangn.common.chat.document;

import com.daangndaangn.common.chat.document.message.ChatMessage;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@ToString
@Getter
@Entity
@Document(collection = "chat_rooms")
public class ChatRoom {

    @Id
    private String id;

    private Long productId;

    private String productImage;

    private Long firstUserId;

    private Long secondUserId;

    private String identifier;

    @Field("chat_messages")
    List<ChatMessage> chatMessages = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Long getOtherPersonId(Long userId) {
        checkArgument(userId != null, "userId must not be null");

        if (userId.equals(firstUserId)) {
            return secondUserId;
        }
        if (userId.equals(secondUserId)) {
            return firstUserId;
        }

        throw new IllegalArgumentException("채팅방에 존재하지 않는 userId 입니다.");
    }

    @Builder
    private ChatRoom(String id,
                     Long productId,
                     String productImage,
                     Long firstUserId,
                     Long secondUserId,
                     String identifier) {

        this.id = id;
        this.productId = productId;
        this.productImage = productImage;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.identifier = identifier;
    }
}
