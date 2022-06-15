package com.daangndaangn.common.chat.repository.chatroom;

import com.daangndaangn.common.chat.document.message.ChatMessage;

public interface ChatRoomCustomRepository {
    long insertChatMessage(String id, ChatMessage chatMessage);
}
