package com.daangndaangn.common.chat.repository.room;

import com.daangndaangn.common.chat.document.room.ChattingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingRoomRepository extends MongoRepository<ChattingRoom, String> {
}
