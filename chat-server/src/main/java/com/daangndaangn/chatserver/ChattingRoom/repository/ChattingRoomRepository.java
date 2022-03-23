package com.daangndaangn.chatserver.ChattingRoom.repository;

import com.daangndaangn.chatserver.ChattingRoom.model.ChattingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingRoomRepository extends MongoRepository<ChattingRoom, String> {
}
