package com.daangndaangn.common.chat.document;

import com.daangndaangn.common.chat.document.message.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@ToString
@Getter
@Document(collection = "chat_rooms")
public class ChatRoom {

    @Id
    private String id;

    @Field("product_id")
    private Long productId;

    @Field("product_image")
    private String productImage;

    @Field("first_user_id")
    private Long firstUserId;

    @Field("second_user_id")
    private Long secondUserId;

    private String identifier;

    @Field("chat_messages")
    List<ChatMessage> chatMessages = new ArrayList<>();

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Long getOtherUserId(Long userId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

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

        checkArgument(productId != null, "productId 값은 필수입니다.");
        checkArgument(firstUserId != null, "firstUserId 값은 필수입니다.");
        checkArgument(secondUserId != null, "secondUserId 값은 필수입니다.");
        checkArgument(isNotEmpty(identifier), "identifier 값은 필수입니다.");

        this.id = id;
        this.productId = productId;
        this.productImage = productImage;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.identifier = identifier;
    }
}
