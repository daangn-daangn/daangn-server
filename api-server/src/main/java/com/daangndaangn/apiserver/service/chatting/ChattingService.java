package com.daangndaangn.apiserver.service.chatting;

import com.daangndaangn.common.chat.document.room.ChattingRoom;

public interface ChattingService {

    public ChattingRoom findChatting(Long userId);
    public void createChatting(Long productId, Long userId);
}
