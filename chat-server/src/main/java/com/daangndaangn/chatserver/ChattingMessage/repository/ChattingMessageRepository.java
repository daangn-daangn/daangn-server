package com.daangndaangn.chatserver.ChattingMessage.repository;

import com.daangndaangn.chatserver.ChattingMessage.model.ChattingMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingMessageRepository extends MongoRepository<ChattingMessage, String> {
}
