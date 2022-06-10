package com.daangndaangn.chatserver.service;

import com.daangndaangn.common.chat.document.message.ChatMessage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatMessageService {
    String create(String roomId, Long senderId, Integer messageTypeCode, String message);

    List<ChatMessage> getChatMessages(String roomId, Pageable pageable);
}
