package com.daangndaangn.apiserver.service.chatroom;

import com.daangndaangn.common.chat.document.ChatRoom;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatRoomService {
    ChatRoom create(Long productId, List<Long> userIds);

    List<ChatRoom> getChatRooms(Long userId, Pageable pageable);

    /**
     * for 채팅방 메시지 목록 조회 API
     */
    ChatRoom getChatRoomWithMessages(String id, int page);

    ChatRoom getChatRoom(String id);

    String toIdentifier(Long userId1, Long userId2);

    /**
     * for 채팅 메시지 보내기 API
     */
    long addChatMessage(String id, Long senderId, int messageTypeCode, String message);

    long getChatRoomMessageSize(String id);
}
