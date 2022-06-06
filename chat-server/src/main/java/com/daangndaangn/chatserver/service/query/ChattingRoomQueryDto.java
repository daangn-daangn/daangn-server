package com.daangndaangn.chatserver.service.query;

import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChattingRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChattingRoomQueryDto {
    private String profileImage;
    private String nickname;
    private String location;
    private String productImage;
    private String lastChat;
    private LocalDateTime createdAt;

    public static ChattingRoomQueryDto of(ChattingRoom chattingRoom, User user) {
        return ChattingRoomQueryDto.builder()
                .profileImage(user.getProfileUrl())
                .nickname(user.getNickname())
                .location(user.getLocation().getAddress())
                .productImage(chattingRoom.getProductImage())
                .lastChat("lastChat")
                .createdAt(chattingRoom.getCreatedAt())
                .build();
    }
}
