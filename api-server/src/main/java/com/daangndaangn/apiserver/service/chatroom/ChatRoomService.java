package com.daangndaangn.apiserver.service.chatroom;

import com.daangndaangn.common.chat.document.ChatRoom;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatRoomService {
    ChatRoom create(Long productId, List<Long> userIds);

    List<ChatRoom> getChattingRooms(Long userId, Pageable pageable);

    ChatRoom getChattingRoom(String id);

    String toIdentifier(Long userId1, Long userId2);
}
