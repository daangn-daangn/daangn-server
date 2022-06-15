package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
import com.daangndaangn.chatserver.controller.message.ChatMessageResponse.GetResponse;
import com.daangndaangn.chatserver.controller.MessageSender;
import com.daangndaangn.chatserver.service.message.ChatMessageService;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequestMapping("/chat/messages")
@RestController
@RequiredArgsConstructor
public class ChatMessageApiController {

    private final ChatMessageService chatMessageService;
    private final MessageSender messageSender;

    /**
     * 채팅메시지 보내기 API
     *
     * POST /chat/messages
     */
    @PostMapping
    public void sendMessage(@RequestBody CreateRequest message) {
        log.info("call POST /chat/messages");

        long addCount = chatMessageService.addChatMessage(message.getRoomId(),
                                                          message.getSenderId(),
                                                          message.getReceiverId(),
                                                          message.getMessageType(),
                                                          message.getMessage());

        log.info("addCount: {}", addCount);

        if (addCount > 0) {
            messageSender.send(message);
        }
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

        log.info("call GET /chat/messages?room_id=");
        ChatRoom chatRoom = chatMessageService.getChatRoomWithMessages(roomId, page);

        List<GetResponse> getResponses = chatRoom.getChatMessages().stream()
                .map(GetResponse::from)
                .collect(toList());

        log.info("chatRoom.getChatMessages().size() = {}",chatRoom.getChatMessages().size());
        log.info("chatRoom.getChatMessages() = {}",chatRoom.getChatMessages());

        return OK(getResponses);
    }

    @PutMapping("/read-size")
    public ApiResult<Void> updateReadMessageSize(@AuthenticationPrincipal JwtAuthentication authentication,
                                                 @RequestBody ChatMessageRequest.UpdateRequest request) {

        log.info("authentication.getId(): {}", authentication.getId());
        log.info("request.getRoomId(): {}", request.getRoomId());

        chatMessageService.updateReadMessageSize(request.getRoomId(), authentication.getId());

        return OK(null);
    }
}
