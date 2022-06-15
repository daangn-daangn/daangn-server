package com.daangndaangn.common.chat.repository.chatroom;

import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.message.ChatMessage;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {

    private final MongoTemplate mongoTemplate;

    public long insertChatMessage(String id, ChatMessage chatMessage) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().push("chat_messages", chatMessage);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ChatRoom.class);
        return updateResult.getModifiedCount();
    }
}
