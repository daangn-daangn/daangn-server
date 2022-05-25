package com.daangndaangn.common.chat.document.room;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Document(collation = "chatting-room")
public class ChattingRoom {

    @Id
    private String id;

    private Long userId;

    private ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();;

    private LocalDateTime createdAt;

    private ChattingRoom(Long userId){
        this.userId = userId;
    }

    public static ChattingRoom userIdOf(Long userId){
        return new ChattingRoom(userId);
    }

    public void addChatting(Long productId, String chattingRoomId){
        map.put(productId, chattingRoomId);
    }

//    public void removeChatting(int productId){
//        map.remove(productId);
//    }
}
