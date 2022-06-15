package com.daangndaangn.chatserver.service.chatroom;

import com.daangndaangn.common.chat.document.ChatRoom;

/**
 * ChatRoom 도메인 관련해서 아래 두 기능만 분리
 *  1. 채팅 메시지 보내기 기능
 *  2. 채팅방 메시지 목록 가져오기 기능
 */
public interface ChatMessageService {
    /**
     * for 채팅 메시지 보내기 API
     */
    long addChatMessage(String id, Long senderId, int messageTypeCode, String message);

    /**
     * for 채팅방 메시지 목록 조회 API
     */
    ChatRoom getChatRoomWithMessages(String id, int page);

    /**
     * for 읽은 메시지 갯수 갱신 API
     */
    void updateReadMessageSize(String id, Long userId);
}
