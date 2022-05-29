package com.daangndaangn.common.chat.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Builder
@Getter
@Document(collection = "chatting_info")
public class ChattingInfo {

    @Id
    private String id;

    private String chattingId;

    private Long productId;

    private Long sellerId;

    private Long buyerId;
}
