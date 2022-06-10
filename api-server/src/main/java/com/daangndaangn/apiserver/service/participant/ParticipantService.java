package com.daangndaangn.apiserver.service.participant;

import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParticipantService {
    Participant create(Long userId, ChatRoom chatRoom);

    List<Participant> getParticipants(Long userId, Pageable pageable);
}
