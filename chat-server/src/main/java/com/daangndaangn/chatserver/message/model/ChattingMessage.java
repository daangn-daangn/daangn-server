package com.daangndaangn.chatserver.message.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Document(collation = "chatting-message")
public class ChattingMessage {

    @Id
    private String id;

    private ArrayList<MessageInfo> messages = new ArrayList<>();

    private LocalDateTime updatedAt;

    public void addMessage(String sender, String message){
        LocalDateTime now = LocalDateTime.now();
        MessageInfo messageInfo = MessageInfo.builder()
                .sender(sender)
                .message(message)
                .createdAt(now)
                .build();
        this.messages.add(messageInfo);
        this.updatedAt = now;
    }
}
