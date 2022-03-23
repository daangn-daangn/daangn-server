package com.daangndaangn.chatserver.ChattingRoom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collation = "chatting-room")
public class ChattingRoom {

    @Id
    private String id;

    private String nickname;

    private HashMap<Integer, String> map;

    public ChattingRoom(String nickname){
        this.nickname = nickname;
        this.map = new HashMap<>();
    }

    public void addChatting(int productId, String chattingRoomId){
        map.put(productId, chattingRoomId);
    }

    public void removeChatting(int productId){
        map.remove(productId);
    }
}
