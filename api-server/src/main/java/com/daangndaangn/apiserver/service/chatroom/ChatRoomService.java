package com.daangndaangn.apiserver.service.chatroom;

import com.daangndaangn.common.chat.document.ChatRoom;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * ChatRoom 도메인 관련해서 아래 세 기능만 chat-server로 분리
 *  1. 채팅 메시지 보내기 기능
 *  2. 채팅방 메시지 목록 가져오기 기능
 *  3. 채팅방 내 메시지 읽은 갯수 갱신
 *
 * 나머지 기능은 전부 api-server에서 처리
 */
public interface ChatRoomService {
    ChatRoom create(Long productId, List<Long> userIds);

    List<ChatRoom> getChatRooms(Long userId, Pageable pageable);

    ChatRoom getChatRoom(String id);

    String toIdentifier(Long userId1, Long userId2);

    long getChatRoomMessageSize(String id);
}
