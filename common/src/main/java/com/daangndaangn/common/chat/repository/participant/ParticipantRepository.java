package com.daangndaangn.common.chat.repository.participant;

import com.daangndaangn.common.chat.document.Participant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends MongoRepository<Participant, String>, ParticipantCustomRepository {
    /**
     * Participant 정보를 chatRoomId와 userId로 조회
     */
    Optional<Participant> findByChatRoomIdAndUserId(String chatRoomId, Long userId);

    List<Participant> findAllByUserIdAndOutIsFalseOrderByUpdatedAtDesc(Long userId, Pageable pageable);
}
