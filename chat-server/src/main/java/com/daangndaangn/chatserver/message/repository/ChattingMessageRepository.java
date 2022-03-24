package com.daangndaangn.chatserver.message.repository;

import com.daangndaangn.chatserver.message.model.ChattingMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingMessageRepository extends MongoRepository<ChattingMessage, String> {
}
