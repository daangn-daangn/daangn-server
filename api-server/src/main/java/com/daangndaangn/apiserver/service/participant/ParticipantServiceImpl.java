package com.daangndaangn.apiserver.service.participant;

import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Transactional(readOnly = true, value = "mongoTransactionManager")
@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    @Override
    @Transactional(value = "mongoTransactionManager")
    public Participant create(Long userId, String chatRoomId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");
        checkArgument(isNotEmpty(chatRoomId), "chatRoomId 값은 필수입니다.");

        Participant participant = Participant.builder()
                .userId(userId)
                .chatRoomId(chatRoomId)
                .build();

        return participantRepository.save(participant);
    }

    @Override
    public boolean isParticipant(String chatRoomId, Long userId) {
        checkArgument(isNotEmpty(chatRoomId), "chatRoomId 값은 필수입니다.");
        checkArgument(userId != null, "userId 값은 필수입니다.");

        return participantRepository.existsByChatRoomIdAndUserId(chatRoomId, userId);
    }

    @Override
    public Participant getParticipant(String chatRoomId, Long userId) {
        checkArgument(isNotEmpty(chatRoomId), "chatRoomId 값은 필수입니다.");
        checkArgument(userId != null, "userId 값은 필수입니다.");

        return participantRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new NotFoundException(Participant.class,
                        String.format("chatRoomId = %s, userId = %s", chatRoomId, userId)));
    }

    @Override
    public List<Participant> getParticipants(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        return participantRepository.findAllByUserIdAndOutIsFalseOrderByUpdatedAtDesc(userId, pageable);
    }
}
