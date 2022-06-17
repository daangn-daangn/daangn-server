package com.daangndaangn.chatserver.service.participant;

import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Transactional(readOnly = true, value = "mongoTransactionManager")
@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    @Override
    public Participant getParticipant(String chatRoomId, Long userId) {
        checkArgument(isNotEmpty(chatRoomId), "chatRoomId 값은 필수입니다.");
        checkArgument(userId != null, "userId 값은 필수입니다.");

        return participantRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new NotFoundException(Participant.class,
                        String.format("chatRoomId = %s, userId = %s", chatRoomId, userId)));
    }

    @Override
    @Transactional(value = "mongoTransactionManager")
    public void inviteUser(String chatRoomId, Long userId) {
        Participant participant = getParticipant(chatRoomId, userId);
        participant.update(false);
        participantRepository.save(participant);
    }

    @Override
    @Transactional(value = "mongoTransactionManager")
    public void deleteUser(String chatRoomId, Long userId) {
        Participant participant = getParticipant(chatRoomId, userId);
        participant.update(true);
        participantRepository.save(participant);
    }
}
