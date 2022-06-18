package com.daangndaangn.apiserver.service.participant;

import com.daangndaangn.common.chat.document.Participant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParticipantService {
    /**
     * 채팅방 생성 시 2개의 Participant를 생성
     */
    Participant create(Long userId, String chatRoomId);

    Participant getParticipant(String id);

    /**
     * 채팅방 참여자인지 확인
     */
    boolean isParticipant(String chatRoomId, Long userId);

    /**
     * findByUserIdWithChatRoomId
     */
    Participant getParticipant(String chatRoomId, Long userId);

    /**
     * 최신 업데이트 기준으로 채팅방을 불러오기 위한 기능
     */
    List<Participant> getParticipants(Long userId, Pageable pageable);
}
