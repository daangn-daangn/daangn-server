package com.daangndaangn.common.chat.document;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 *  message : {
 *               sender : "닉네임 or 이메일",
 *               text   : 채팅 메세지 내용,
 *               time   : 년/월/일/시간
 *  },
 */
@Getter
public class ChattingMessage {
    private String sender;
    private String message;
    private String imgUrl;
    private LocalDateTime time;
}
