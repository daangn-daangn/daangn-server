package com.daangndaangn.common.chat.repository.participant;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipantCustomRepository {
    long synchronizeUpdatedAt(String chatRoomId, List<Long> userIds, LocalDateTime updatedAt);
}
