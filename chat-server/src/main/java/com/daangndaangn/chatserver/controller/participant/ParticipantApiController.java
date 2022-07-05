package com.daangndaangn.chatserver.controller.participant;

import com.daangndaangn.chatserver.controller.MessageSender;
import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
import com.daangndaangn.chatserver.controller.participant.ParticipantRequest.InviteRequest;
import com.daangndaangn.chatserver.service.participant.ParticipantService;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.controller.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.daangndaangn.common.controller.ApiResult.OK;

@RequestMapping("/chat/chat-rooms")
@RestController
@RequiredArgsConstructor
public class ParticipantApiController {

    private final ParticipantService participantService;
    private final MessageSender messageSender;

    /**
     * 채팅방 초대하기
     *
     * PUT /chat/chat-rooms/invitation/:chat-room-id
     */
    @PutMapping("/invitation/{chatRoomId}")
    public ApiResult<Void> inviteChatRoom(@AuthenticationPrincipal JwtAuthentication authentication,
                                          @PathVariable("chatRoomId") String chatRoomId,
                                          @Valid @RequestBody InviteRequest request) {

        if (request.getUserId().equals(authentication.getId())) {
            throw new IllegalArgumentException("자기 자신은 초대할 수 없습니다.");
        }

        participantService.inviteUser(chatRoomId, authentication.getId(), request.getUserId());

        return OK();
    }

    /**
     * 채팅방 나가기
     *
     * PUT /chat/chat-rooms/exit/:chat-room-id
     */
    @PutMapping("/exit/{chatRoomId}")
    public ApiResult<Void> exitChatRoom(@AuthenticationPrincipal JwtAuthentication authentication,
                                        @PathVariable("chatRoomId") String chatRoomId) {

        if (participantService.isParticipant(chatRoomId, authentication.getId())) {
            participantService.deleteUser(chatRoomId, authentication.getId());

            messageSender.send(CreateRequest.of(chatRoomId, authentication.getId()));
            return OK();
        }

        throw new UnauthorizedException("채팅방 참여자만 채팅방 나가기를 할 수 있습니다.");
    }
}
