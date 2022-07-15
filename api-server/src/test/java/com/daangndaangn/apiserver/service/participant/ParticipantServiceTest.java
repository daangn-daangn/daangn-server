package com.daangndaangn.apiserver.service.participant;


import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
import com.daangndaangn.common.error.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
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

    private User mockUser;
    private String mockChatRoomId = "testMockChatRoomId";
    private Participant mockParticipant;

    @BeforeEach
    public void init() {
        mockUser = User.builder()
                .id(1L)
                .oauthId(12345L)
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

    @Test
    @DisplayName("채팅방_참여자이면_true를_반환한다")
    public void isParticipant1() {
        //given
        given(participantRepository.existsByChatRoomIdAndUserId(anyString(), anyLong())).willReturn(true);

        String mockChatRoomId = "mockChatRoomId";

        //when
        boolean result = participantService.isParticipant(mockChatRoomId, mockParticipant.getUserId());

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("채팅방_참여자가_아니면_false를_반환한다")
    public void isParticipant2() {
        //given
        given(participantRepository.existsByChatRoomIdAndUserId(anyString(), anyLong())).willReturn(false);

        String invalidChatRoomId = "invalidChatRoomId";

        //when
        boolean result = participantService.isParticipant(invalidChatRoomId, mockParticipant.getUserId());

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("chatRoomId와 userId로 참여자를 조회할 수 있다")
    public void getParticipant1() {
        //given
        given(participantRepository.findByChatRoomIdAndUserId(anyString(), anyLong())).willReturn(Optional.ofNullable(mockParticipant));

        //when
        Participant participant = participantService.getParticipant(mockChatRoomId, mockUser.getId());

        //then
        assertThat(participant).isEqualTo(mockParticipant);
        verify(participantRepository).findByChatRoomIdAndUserId(anyString(), anyLong());
    }

    @Test
    @DisplayName("chatRoomId와 userId에 해당하는 participant 정보가 없을 시 예외를 반환한다")
    public void getParticipant2() {
        //given
        given(participantRepository.findByChatRoomIdAndUserId(anyString(), anyLong())).willReturn(Optional.empty());

        //when
        Assertions.assertThatThrownBy(() -> participantService.getParticipant(mockChatRoomId, mockUser.getId()))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(participantRepository).findByChatRoomIdAndUserId(anyString(), anyLong());
    }

    @Test
    @DisplayName("사용자가 실제로 참여중인 채팅방 리스트를 조회할 수 있다")
    public void getParticipants1() {
        //given
        Pageable mockPageable = PageRequest.of(0, 5);

        List<Participant> participants = Arrays.asList(mockParticipant, mockParticipant, mockParticipant);
        given(participantRepository.findAllByUserIdAndOutIsFalseOrderByUpdatedAtDesc(anyLong(), any()))
                .willReturn(participants);

        //when
        List<Participant> result = participantService.getParticipants(mockUser.getId(), mockPageable);

        //then
        assertThat(result.size()).isEqualTo(participants.size());
        verify(participantRepository).findAllByUserIdAndOutIsFalseOrderByUpdatedAtDesc(anyLong(), any());
    }

    @Test
    @DisplayName("사용자가 실제로 참여중인 채팅방 리스트를 조회 시 userId는 필수이다")
    public void getParticipants2() {
        //given
        Long invalidUserId = null;
        Pageable mockPageable = PageRequest.of(0, 5);

        //when
        Assertions.assertThatThrownBy(() -> participantService.getParticipants(invalidUserId, mockPageable))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(participantRepository, never()).findAllByUserIdAndOutIsFalseOrderByUpdatedAtDesc(anyLong(), any());
    }
}