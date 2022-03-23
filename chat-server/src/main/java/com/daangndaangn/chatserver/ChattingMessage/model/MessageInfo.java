package com.daangndaangn.chatserver.ChattingMessage.model;

import lombok.Builder;

import java.time.LocalDateTime;


public class MessageInfo {

    private String sender;

    private String message;

    private LocalDateTime createdAt;

    @Builder
    public MessageInfo(String sender, String message, LocalDateTime createdAt){
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }
}
