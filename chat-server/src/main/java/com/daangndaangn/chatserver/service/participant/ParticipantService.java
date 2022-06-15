package com.daangndaangn.chatserver.service.participant;

import com.daangndaangn.common.chat.document.Participant;

public interface ParticipantService {

    Participant getParticipant(String chatRoomId, Long userId);

    /**
     * 채팅방 초대하기
     */
    void inviteUser(String chatRoomId, Long userId);

    /**
     * 채팅방 나가기
     */
    void deleteUser(String chatRoomId, Long userId);
}
