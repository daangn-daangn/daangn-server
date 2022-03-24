package com.daangndaangn.chatserver.room.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Document(collation = "chatting-room")
public class ChattingRoom {

    @Id
    private String id;

    private String nickname;

    private ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();;

    private LocalDateTime createdAt;

    private ChattingRoom(String nickname){
        this.nickname = nickname;
    }

    public static ChattingRoom nicknameOf(String nickname){
        return new ChattingRoom(nickname);
    }

    public void addChatting(int productId, String chattingRoomId){
        map.put(productId, chattingRoomId);
    }

    public void removeChatting(int productId){
        map.remove(productId);
    }
}
