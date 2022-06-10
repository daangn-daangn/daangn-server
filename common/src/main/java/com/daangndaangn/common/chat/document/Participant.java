package com.daangndaangn.common.chat.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 채팅방 참여자의 정보
 *
 * 하나의 ChattingRoom에 대해 2개의 Participant가 생성된다.
 */
@Getter
@Document(collection = "participant")
public class Participant {
    @Id
    private String id;

    private Long userId;

    // private String chattingRoomId;
    @DBRef
    private ChatRoom chatRoom;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private Participant(String id, Long userId, ChatRoom chatRoom) {
        this.id = id;
        this.userId = userId;
        this.chatRoom = chatRoom;
    }
}
