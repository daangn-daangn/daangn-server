package com.daangndaangn.common.chat.repository.message;

import com.daangndaangn.common.chat.document.message.ChattingMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingMessageRepository extends MongoRepository<ChattingMessage, String> {
}
