package com.daangndaangn.apiserver.service.chatroom.query;

import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomQueryDto {
    private String profileImage;
    private String nickname;
    private String location;
    private String productImage;
    private String lastChat;
    private LocalDateTime createdAt;

    public static ChatRoomQueryDto of(ChatRoom chattingRoom, User user) {
        return ChatRoomQueryDto.builder()
                .profileImage(user.getProfileUrl())
                .nickname(user.getNickname())
                .location(user.getLocation().getAddress())
                .productImage(chattingRoom.getProductImage())
                .lastChat("lastChat")
                .createdAt(chattingRoom.getCreatedAt())
                .build();
    }
}
