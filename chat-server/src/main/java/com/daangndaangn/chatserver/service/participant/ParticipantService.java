package com.daangndaangn.chatserver.service.participant;

import com.daangndaangn.common.chat.document.Participant;

public interface ParticipantService {

    Participant getParticipant(String chatRoomId, Long userId);

    /**
     * 채팅방 참여자인지 확인
     */
    boolean isParticipant(String chatRoomId, Long userId);

    /**
     * 채팅방 초대하기
     */
    void inviteUser(String chatRoomId, Long inviterId, Long inviteeId);

    /**
     * 채팅방 나가기
     */
    void deleteUser(String chatRoomId, Long userId);
}
