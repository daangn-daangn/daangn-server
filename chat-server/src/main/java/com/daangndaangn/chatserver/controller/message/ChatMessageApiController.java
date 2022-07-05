package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.ImageUploadRequest;
import com.daangndaangn.chatserver.controller.message.ChatMessageResponse.GetResponse;
import com.daangndaangn.chatserver.controller.MessageSender;
import com.daangndaangn.chatserver.controller.message.ChatMessageResponse.ImageUploadResponse;
import com.daangndaangn.chatserver.service.message.ChatMessageService;
import com.daangndaangn.chatserver.service.participant.ParticipantService;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.message.MessageType;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.controller.ApiResult;
import com.daangndaangn.common.controller.ErrorResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.daangndaangn.common.controller.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@RequestMapping("/chat/messages")
@RestController
@RequiredArgsConstructor
public class ChatMessageApiController {

    private final ChatMessageService chatMessageService;
    private final ParticipantService participantService;
    private final MessageSender messageSender;
    private final ChatMessageUtils chatMessageUtils;
    private final PresignerUtils presignerUtils;

    /**
     * 채팅메시지 보내기 API
     *
     * POST /chat/messages
     *
     * success: void
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResult<?>>> sendMessage(@Valid @RequestBody CreateRequest message) {

        return chatMessageService.addChatMessage(message.getRoomId(),
                                                 message.getSenderId(),
                                                 message.getReceiverId(),
                                                 message.getMessageType(),
                                                 message.getMessage(),
                                                 message.getImgFiles()).handle((addCount, throwable) -> {

            if (addCount != null && addCount == 3) {
                CreateRequest convertedMessage = convertMessage(message);
                messageSender.send(convertedMessage);
                return new ResponseEntity<>(OK(), HttpStatus.OK);
            }

            return ErrorResponseEntity.from(throwable, true);
        });
    }

    /**
     * 메시지 타입이 이미지가 아닌경우 => 그대로 리턴
     * 메시지 타입이 이미지인 경우 => requestUrl을 presignedUrl로 변환해서 메시지를 보내준다.
     */
    private CreateRequest convertMessage(CreateRequest message) {
        if (!MessageType.IMAGE.getCode().equals(message.getMessageType())) {
            return message;
        }

        List<String> presignedUrls = message.getImgFiles().stream()
                .map(presignerUtils::getChatRoomPresignedGetUrl).collect(toList());

        return CreateRequest.of(message, presignedUrls);
    }

    /**
     * 채팅이미지 생성 API
     *
     * POST /chat/messages/images
     */
    @PostMapping("/images")
    public ApiResult<List<ImageUploadResponse>> createChatImages(@Valid @RequestBody
                                                                 ImageUploadRequest imageUploadRequest) {

        return OK(chatMessageUtils.toImageUploadResponses(imageUploadRequest.getImgFiles()));
    }

    /**
     * 채팅방 내 메시지 목록 조회 API
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
                .map(chatMessage -> {
                    if (chatMessage.getMessageType().equals(MessageType.IMAGE)) {
                        List<String> presignedUrl = getPresignedUrl(chatMessage.getImgUrls());
                        return GetResponse.of(chatMessage, presignedUrl);
                    }
                    return GetResponse.from(chatMessage);
                })
                .collect(toList());

        return OK(getResponses);
    }

    private List<String> getPresignedUrl(List<String> imgUrls) {
        return imgUrls.stream()
                .map(presignerUtils::getChatRoomPresignedGetUrl)
                .collect(toList());
    }

    @PutMapping("/read-size")
    public ApiResult<Void> updateReadMessageSize(@AuthenticationPrincipal JwtAuthentication authentication,
                                                 @RequestBody ChatMessageRequest.UpdateRequest request) {

        if (!participantService.isParticipant(request.getRoomId(), authentication.getId())) {
            throw new UnauthorizedException("자신이 참여중인 채팅방이 아닙니다.");
        }

        chatMessageService.updateReadMessageSize(request.getRoomId(), authentication.getId());

        return OK();
    }
}
