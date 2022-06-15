package com.daangndaangn.apiserver.controller.chatroom;

import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class ChatRoomResponse {

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateResponse {
        private String roomId;

        public static CreateResponse from(String chattingRoomId) {
            return new CreateResponse(chattingRoomId);
        }
    }

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SimpleResponse {
        private String chatRoomId;
        private String participant;
        private String participantImage;
        private String location;
        private String productImage;
        private String lastChat;
        private Long notReadChatCount;
        private Long lastReadMessageSize;
        private LocalDateTime updatedAt;

        public static SimpleResponse of(ChatRoom chatRoom,
                                        User user,
                                        String profileImage,
                                        Participant participant,
                                        String productImage,
                                        long notReadChatCount) {

            return SimpleResponse.builder()
                    .chatRoomId(chatRoom.getId())
                    .participant(user.getNickname())
                    .participantImage(profileImage)
                    .location(isEmpty(user.getLocation()) ?
                            null : user.getLocation().getAddress())
                    .productImage(productImage)
                    .lastChat(isEmpty(chatRoom.getChatMessages()) ?
                            null : chatRoom.getChatMessages().get(0).getMessage())
                    .notReadChatCount(notReadChatCount)
                    .lastReadMessageSize(participant.getReadMessageSize())
                    .updatedAt(chatRoom.getUpdatedAt())
                    .build();
        }
    }
}
