package com.daangndaangn.common.chat.repository.participant;

import com.daangndaangn.common.chat.document.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends MongoRepository<Participant, String> {
    /**
     * Participant 정보를 chatRoomId와 userId로 조회
     */
    Optional<Participant> findByChatRoomIdAndUserId(String chatRoomId, Long userId);
}
