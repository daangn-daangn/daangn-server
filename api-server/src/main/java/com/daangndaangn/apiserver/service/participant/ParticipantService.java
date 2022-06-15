package com.daangndaangn.apiserver.service.participant;

import com.daangndaangn.common.chat.document.Participant;

public interface ParticipantService {
    /**
     * 채팅방 생성 시 2개의 Participant를 생성
     */
    Participant create(Long userId, String chatRoomId);

    Participant getParticipant(String id);

    /**
     * findByUserIdWithChatRoomId
     */
    Participant getParticipant(String chatRoomId, Long userId);

    /**
     * 채팅방 나가기
     */
    void delete(String id);

    /**
     * 사용자가 채팅방 나갈 때 채팅방 내 읽은 메시지 갯수 갱신
     */
    void updateReadMessageSize(String participantId, String chatRoomId);
}
