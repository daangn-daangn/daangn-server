package com.daangndaangn.chatserver.service.message;

import com.daangndaangn.common.chat.document.ChatRoom;

import java.util.concurrent.CompletableFuture;

/**
 * ChatRoom 도메인 관련해서 아래 세 기능만 분리
 *  1. 채팅 메시지 보내기 기능
 *  2. 채팅방 메시지 목록 가져오기 기능
 *  3. 채팅방 내 메시지 읽은 갯수 갱신
 */
public interface ChatMessageService {
    /**
     * for 채팅 메시지 보내기 API
     */
    CompletableFuture<Long> addChatMessage(String id, Long senderId, Long receiverId, int messageTypeCode, String message);

    /**
     * for 채팅방 메시지 목록 조회 API
     */
    ChatRoom getChatRoomWithMessages(String id, int page);

    /**
     * for 읽은 메시지 갯수 갱신 API
     */
    void updateReadMessageSize(String id, Long userId);
}
