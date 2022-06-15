package com.daangndaangn.common.chat.document;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 채팅방 참여자의 정보
 *
 * 하나의 ChattingRoom에 대해 2개의 Participant가 생성된다.
 */
@ToString
@Getter
@Document(collection = "participants")
public class Participant {
    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    @Field("chat_room_id")
    private String chatRoomId;

    // 참여자가 해당 채팅방에서 마지막으로 읽은 message 갯수
    @Field("read_message_size")
    private Long readMessageSize;

    @Field("out")
    private boolean out;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void update(Long readMessageSize) {
        this.readMessageSize = isEmpty(readMessageSize) ? this.readMessageSize : readMessageSize;
    }

    public void update(boolean out) {
        this.out = out;
    }

    @Builder
    private Participant(String id, Long userId, String chatRoomId) {
        checkArgument(userId != null, "userId must not be null");
        checkArgument(isNotEmpty(chatRoomId), "chatRoomId must not be null");

        this.id = id;
        this.userId = userId;
        this.chatRoomId = chatRoomId;
        this.readMessageSize = 0L;
        this.out = false;
    }
}
