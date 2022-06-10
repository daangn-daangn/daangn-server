package com.daangndaangn.apiserver.service.participant;

import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    @Override
    @Transactional(value = "mongoTransactionManager")
    public Participant create(Long userId, ChatRoom chatRoom) {
        Participant participant = Participant.builder()
                .userId(userId)
                .chatRoom(chatRoom)
                .build();

        return participantRepository.save(participant);
    }

    @Override
    public List<Participant> getParticipants(Long userId, Pageable pageable) {
        return participantRepository.findAllByUserId(userId, pageable);
    }
}
