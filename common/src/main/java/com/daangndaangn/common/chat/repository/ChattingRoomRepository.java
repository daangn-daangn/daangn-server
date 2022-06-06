package com.daangndaangn.common.chat.repository;

import com.daangndaangn.common.chat.document.ChattingRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingRoomRepository extends MongoRepository<ChattingRoom, String> {
    Optional<ChattingRoom> findByProductIdAndIdentifier(Long productId, String identifier);
    List<ChattingRoom> findAllByFirstUserIdOrSecondUserId(Long firstUserId, Long secondUserId, Pageable pageable);
    Long countAllByProductId(Long productId);
    boolean existsByProductIdAndIdentifier(Long productId, String identifier);
}
