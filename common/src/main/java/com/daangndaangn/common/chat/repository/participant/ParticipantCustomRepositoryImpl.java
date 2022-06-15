package com.daangndaangn.common.chat.repository.participant;

import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParticipantCustomRepositoryImpl implements ParticipantCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public long synchronizeUpdatedAt(String chatRoomId, List<Long> userIds, LocalDateTime updatedAt) {
        Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId)
                                                            .and("userId").in(userIds));

        Update update = new Update().set("updated_at", updatedAt);

        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Participant.class);
        return updateResult.getModifiedCount();
    }
}
