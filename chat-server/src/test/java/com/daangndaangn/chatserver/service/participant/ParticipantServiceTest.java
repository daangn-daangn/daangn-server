package com.daangndaangn.chatserver.service.participant;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
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
import static org.mockito.Mockito.times;
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
                .location(Location.from("테스트 Address 입니다."))
                .build();

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
    public void 채팅방을_초대하면_상대방의_out_상태가_false가_된다() {
        //given
        given(participantRepository.findByChatRoomIdAndUserId(anyString(), anyLong()))
                .willReturn(Optional.ofNullable(mockParticipant));
        given(participantRepository.save(any())).willReturn(mockParticipant);
        participantService.deleteUser(mockChatRoomId, mockUser.getId());
        boolean beforeDelete = mockParticipant.isOut();

        //when
        participantService.inviteUser(mockChatRoomId, mockUser.getId());
        boolean afterDelete = mockParticipant.isOut();

        //then
        verify(participantRepository, times(2)).findByChatRoomIdAndUserId(anyString(), anyLong());
        verify(participantRepository, times(2)).save(any());
        assertThat(beforeDelete).isEqualTo(true);
        assertThat(afterDelete).isEqualTo(false);
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