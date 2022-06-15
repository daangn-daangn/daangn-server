package com.daangndaangn.chatserver.controller.participant;

import com.daangndaangn.chatserver.controller.MessageSender;
import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
import com.daangndaangn.chatserver.controller.participant.ParticipantRequest.InviteRequest;
import com.daangndaangn.chatserver.service.participant.ParticipantService;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.daangndaangn.common.web.ApiResult.OK;

@Slf4j
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
                                          @RequestBody InviteRequest request) {

        if (authentication.getId().equals(request.getUserId())) {
            throw new IllegalArgumentException("자기 자신은 초대할 수 없습니다.");
        }

        participantService.inviteUser(chatRoomId, request.getUserId());

        return OK(null);
    }

    /**
     * 채팅방 나가기
     *
     * PUT /chat/chat-rooms/exit/:chat-room-id
     */
    @PutMapping("/exit/{chatRoomId}")
    public ApiResult<Void> exitChatRoom(@AuthenticationPrincipal JwtAuthentication authentication,
                                        @PathVariable("chatRoomId") String chatRoomId) {

        participantService.deleteUser(chatRoomId, authentication.getId());

        messageSender.send(CreateRequest.of(chatRoomId, authentication.getId()));

        return OK(null);
    }
}
