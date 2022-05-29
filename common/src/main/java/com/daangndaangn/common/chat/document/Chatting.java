package com.daangndaangn.common.chat.document;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 채팅방1 id : {
 * 	메세지 내용들 : [
 *          message : {
 *                       sender : "닉네임 or 이메일",
 *                       text   : 채팅 메세지 내용,
 *                       time   : 년/월/일/시간
 *          },
 *          message : {
 *                       sender : "닉네임 or 이메일",
 *                       text   : 채팅 메세지 내용,
 *                       time   : 년/월/일/시간
 *          },
 *          message : {
 *                       sender : "닉네임 or 이메일",
 *                       text   : 채팅 메세지 내용,
 *                       time   : 년/월/일/시간
 *          },
 *        ],
 * 	 수정 시간 : 날짜, (lastUpdatedAt)
 * }
 */
@Getter
@Document("chatting")
public class Chatting {

    @Id
    private String id;

    @Field("messages")
    private List<ChattingMessage> messages;

    private LocalDateTime updatedAt;
}
