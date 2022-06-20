package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
import com.daangndaangn.chatserver.controller.message.ChatMessageResponse.GetResponse;
import com.daangndaangn.chatserver.controller.MessageSender;
import com.daangndaangn.chatserver.service.message.ChatMessageService;
import com.daangndaangn.chatserver.service.participant.ParticipantService;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.web.ApiResult;
import com.daangndaangn.common.web.ErrorResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@RequestMapping("/chat/messages")
@RestController
@RequiredArgsConstructor
public class ChatMessageApiController {

    private final ChatMessageService chatMessageService;
    private final ParticipantService participantService;
    private final MessageSender messageSender;

    /**
     * 채팅메시지 보내기 API
     *
     * POST /chat/messages
     *
     * success: void
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResult<?>>> sendMessage(@RequestBody CreateRequest message) {

        return chatMessageService.addChatMessage(message.getRoomId(),
                                                          message.getSenderId(),
                                                          message.getReceiverId(),
                                                          message.getMessageType(),
                                                          message.getMessage()).handle((addCount, throwable) -> {

            if (addCount != null && addCount == 3) {
                messageSender.send(message);
                return new ResponseEntity<>(OK(null), HttpStatus.OK);
            }

            return ErrorResponseEntity.from(throwable, true);
        });
    }

    /**
     * 채팅방 목록 조회 API
     *
     * GET /chat/messages
     *
     * GET /chat/messages?room_id=1&page=50
     * GET /chat/messages?room_id=1&page=40
     * GET /chat/messages?room_id=1&page=30
     * GET /chat/messages?room_id=1&page=20
     * GET /chat/messages?room_id=1&page=10
     * GET /chat/messages?room_id=1&page=0
     */
    @GetMapping
    public ApiResult<List<GetResponse>> getChatMessages(@RequestParam("room_id") String roomId,
                                                        @RequestParam("page") int page) {

        ChatRoom chatRoom = chatMessageService.getChatRoomWithMessages(roomId, page);

        List<GetResponse> getResponses = chatRoom.getChatMessages().stream()
                .map(GetResponse::from)
                .collect(toList());

        return OK(getResponses);
    }

    @PutMapping("/read-size")
    public ApiResult<Void> updateReadMessageSize(@AuthenticationPrincipal JwtAuthentication authentication,
                                                 @RequestBody ChatMessageRequest.UpdateRequest request) {

        if (!participantService.isParticipant(request.getRoomId(), authentication.getId())) {
            throw new UnauthorizedException("자신이 참여중인 채팅방이 아닙니다.");
        }

        chatMessageService.updateReadMessageSize(request.getRoomId(), authentication.getId());

        return OK(null);
    }
}
