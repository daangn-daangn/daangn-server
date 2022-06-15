package com.daangndaangn.apiserver.service.participant;


import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
import com.daangndaangn.common.error.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @InjectMocks
    private ParticipantServiceImpl participantService;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    private User mockUser;
    private String mockChatRoomId = "testMockChatRoomId";
    private Participant mockParticipant;

    @BeforeEach
    public void init() {
        mockUser = User.builder()
                .id(1L)
                .oauthId(12345L)
                .location(Location.from("테스트 Address 입니다."))
                .build();

        mockParticipant = Participant.builder()
                .id("mockParticipantId")
                .userId(mockUser.getId())
                .chatRoomId(mockChatRoomId)
                .build();
    }

    @Test
    public void userId와_chatRoomId를_입력받으면_participant를_생성할_수_있다() {
        //given
        given(participantRepository.save(any())).willReturn(mockParticipant);

        //when
        Participant savedParticipant = participantService.create(mockUser.getId(), mockChatRoomId);

        //then
        verify(participantRepository).save(any());
        assertThat(savedParticipant.getUserId()).isEqualTo(mockUser.getId());
        assertThat(savedParticipant.getChatRoomId()).isEqualTo(mockChatRoomId);
    }

    @Test
    public void 올바르지않은_userId나_chatRoomId를_입력받으면_예외를_반환한다() {
        //given
        Long invalidUserId = null;
        String invalidChatRoomId = "";

        //when
        assertThrows(IllegalArgumentException.class, () -> participantService.create(invalidUserId, mockChatRoomId));
        assertThrows(IllegalArgumentException.class, () -> participantService.create(mockUser.getId(), invalidChatRoomId));
        assertThrows(IllegalArgumentException.class, () -> participantService.create(invalidUserId, invalidChatRoomId));

        //then
        verify(participantRepository, never()).save(any());
    }

    @Test
    public void 채팅방을_나가면_isOut_상태가_false가_된다() {
        //given
        given(participantRepository.findById(anyString())).willReturn(Optional.ofNullable(mockParticipant));
        given(participantRepository.save(any())).willReturn(mockParticipant);
        boolean beforeDelete = mockParticipant.isOut();

        //when
        participantService.delete(mockParticipant.getId());
        boolean afterDelete = mockParticipant.isOut();

        //then
        verify(participantRepository).findById(anyString());
        verify(participantRepository).save(any());
        assertThat(beforeDelete).isEqualTo(false);
        assertThat(afterDelete).isEqualTo(true);
    }

    @Test
    public void readMessageSize를_내가_속한_채팅방의_채팅_갯수만큼_업데이트_할_수_있다() {
        //given
        long testMessageSize = 25L;
        given(chatRoomRepository.existsById(anyString())).willReturn(true);
        given(participantRepository.findById(anyString())).willReturn(Optional.ofNullable(mockParticipant));
        given(chatRoomRepository.getChatRoomMessageSize(anyString())).willReturn(testMessageSize);
        given(participantRepository.save(any())).willReturn(mockParticipant);
        long beforeUpdate = mockParticipant.getReadMessageSize();

        //when
        participantService.updateReadMessageSize(mockParticipant.getId(), mockChatRoomId);
        long afterUpdate = mockParticipant.getReadMessageSize();

        //then
        verify(chatRoomRepository).existsById(anyString());
        verify(participantRepository).findById(anyString());
        verify(chatRoomRepository).getChatRoomMessageSize(anyString());
        verify(participantRepository).save(any());
        assertThat(beforeUpdate).isEqualTo(0);
        assertThat(afterUpdate).isEqualTo(testMessageSize);
    }

    @Test
    public void 채팅방_id_가_유효하지_않으면_readMessageSize를_업데이트_하지않고_예외를_반환한다() {
        //given
        String invalidChatRoomId = "invalidChatRoomId";

        given(chatRoomRepository.existsById(anyString())).willReturn(false);

        //when
        assertThrows(NotFoundException.class,
                () -> participantService.updateReadMessageSize(mockParticipant.getId(), invalidChatRoomId));

        //then
        verify(chatRoomRepository).existsById(anyString());
        verify(participantRepository, never()).findById(anyString());
        verify(chatRoomRepository, never()).getChatRoomMessageSize(anyString());
        verify(participantRepository, never()).save(any());
    }

    @Test
    public void chatRoomId와_userId의_조합으로_조회할_수_있다() {
        //given
        given(participantRepository.findByChatRoomIdAndUserId(anyString(), anyLong()))
                .willReturn(Optional.ofNullable(mockParticipant));

        //when
        Participant participant = participantService.getParticipant(mockChatRoomId, mockUser.getId());

        //then
        assertThat(participant.getChatRoomId()).isEqualTo(mockChatRoomId);
        assertThat(participant.getUserId()).isEqualTo(mockUser.getId());

        verify(participantRepository).findByChatRoomIdAndUserId(anyString(), anyLong());
    }
}