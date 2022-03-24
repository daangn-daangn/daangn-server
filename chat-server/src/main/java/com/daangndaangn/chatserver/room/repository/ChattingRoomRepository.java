package com.daangndaangn.chatserver.room.repository;

import com.daangndaangn.chatserver.room.model.ChattingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingRoomRepository extends MongoRepository<ChattingRoom, String> {
}
