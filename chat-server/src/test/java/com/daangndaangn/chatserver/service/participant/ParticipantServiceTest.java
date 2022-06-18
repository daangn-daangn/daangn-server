package com.daangndaangn.chatserver.service.participant;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
import com.daangndaangn.common.error.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

        mockUser.update("testNickname", Location.from("test Address"));

        mockParticipant = Participant.builder()
                .id("mockParticipantId")
                .userId(mockUser.getId())
                .chatRoomId(mockChatRoomId)
                .build();
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
    public void 채팅방_참여자이면_true를_반환한다() {
        //given
        given(participantRepository.existsByChatRoomIdAndUserId(anyString(), anyLong())).willReturn(true);

        String mockChatRoomId = "mockChatRoomId";

        //when
        boolean result = participantService.isParticipant(mockChatRoomId, mockParticipant.getUserId());

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void 채팅방_참여자가_아니면_false를_반환한다() {
        //given
        given(participantRepository.existsByChatRoomIdAndUserId(anyString(), anyLong())).willReturn(false);

        String invalidChatRoomId = "invalidChatRoomId";

        //when
        boolean result = participantService.isParticipant(invalidChatRoomId, mockParticipant.getUserId());

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void 채팅방을_초대하면_상대방의_out_상태가_false가_된다() {
        //given
        given(participantRepository.existsByChatRoomIdAndUserId(anyString(), anyLong()))
                .willReturn(true);
        given(participantRepository.findByChatRoomIdAndUserId(anyString(), anyLong()))
                .willReturn(Optional.ofNullable(mockParticipant));
        given(participantRepository.save(any())).willReturn(mockParticipant);
        participantService.deleteUser(mockChatRoomId, mockUser.getId());
        boolean beforeDelete = mockParticipant.isOut();
        long mockInviterId = 11L;

        //when
        participantService.inviteUser(mockChatRoomId, mockInviterId, mockUser.getId());
        boolean afterDelete = mockParticipant.isOut();

        //then
        verify(participantRepository, times(2)).existsByChatRoomIdAndUserId(anyString(), anyLong());
        verify(participantRepository, times(2)).findByChatRoomIdAndUserId(anyString(), anyLong());
        verify(participantRepository, times(2)).save(any());
        assertThat(beforeDelete).isEqualTo(true);
        assertThat(afterDelete).isEqualTo(false);
    }

    @Test
    public void 채팅방에_참여중이지_않던_사용자를_초대하면_예외를_반환한다() {
        //given
        given(participantRepository.existsByChatRoomIdAndUserId(anyString(), anyLong()))
                .willReturn(false);

        long mockInviterId = 11L;

        //when
        Assertions.assertThrows(UnauthorizedException.class,
            () -> participantService.inviteUser(mockChatRoomId, mockInviterId, mockUser.getId()));

        //then
        verify(participantRepository, never()).findByChatRoomIdAndUserId(anyString(), anyLong());
        verify(participantRepository, never()).save(any());
    }

    @Test
    public void 채팅방을_나가면_out_상태가_true가_된다() {
        //given
        given(participantRepository.findByChatRoomIdAndUserId(anyString(), anyLong()))
                .willReturn(Optional.ofNullable(mockParticipant));

        given(participantRepository.save(any())).willReturn(mockParticipant);
        boolean beforeDelete = mockParticipant.isOut();

        //when
        participantService.deleteUser(mockChatRoomId, mockUser.getId());
        boolean afterDelete = mockParticipant.isOut();

        //then
        verify(participantRepository).findByChatRoomIdAndUserId(anyString(), anyLong());
        verify(participantRepository).save(any());
        assertThat(beforeDelete).isEqualTo(false);
        assertThat(afterDelete).isEqualTo(true);
    }
}