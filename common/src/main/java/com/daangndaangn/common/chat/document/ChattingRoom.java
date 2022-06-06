package com.daangndaangn.common.chat.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Document(collection = "chatting_room")
public class ChattingRoom {
    @Id
    private String id;

    private Long productId;

    private String productImage;

    private Long firstUserId;

    private Long secondUserId;

    private String identifier;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private ChattingRoom(String id,
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
