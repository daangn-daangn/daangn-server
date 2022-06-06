package com.daangndaangn.common.chat.repository;

import com.daangndaangn.common.chat.document.ChattingMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingMessageRepository extends MongoRepository<ChattingMessage, String> {
    List<ChattingMessage> findAllByRoomId(String roomId, Pageable pageable);
}
