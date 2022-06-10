package com.daangndaangn.common.chat.repository;

import com.daangndaangn.common.chat.document.Participant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends MongoRepository<Participant, String> {
    List<Participant> findAllByUserId(Long userId, Pageable pageable);
}
