package com.daangndaangn.chatserver.service;

import com.daangndaangn.common.chat.document.ChattingRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Transactional
    public Participant create(Long userId, ChattingRoom chattingRoom) {
        Participant participant = Participant.builder()
                                            .userId(userId)
                                            .chattingRoom(chattingRoom)
                                            .build();

        return participantRepository.save(participant);
    }

    public List<Participant> getParticipants(Long userId, Pageable pageable) {
        return participantRepository.findAllByUserId(userId, pageable);
    }
}
