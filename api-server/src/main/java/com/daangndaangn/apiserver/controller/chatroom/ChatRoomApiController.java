package com.daangndaangn.apiserver.controller.chatroom;

import com.daangndaangn.apiserver.controller.chatroom.ChatRoomResponse.CreateResponse;
import com.daangndaangn.apiserver.controller.chatroom.ChatRoomResponse.SimpleResponse;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.chatroom.ChatRoomService;
import com.daangndaangn.apiserver.service.participant.ParticipantService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequestMapping("/api/chat-rooms")
@RestController
@RequiredArgsConstructor
public class ChatRoomApiController {

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ParticipantService participantService;
    private final PresignerUtils presignerUtils;

    /**
     * 채팅방 생성 POST /api/chat-rooms
     */
    @PostMapping
    public ApiResult<CreateResponse> createChattingRoom(@AuthenticationPrincipal JwtAuthentication authentication,
                                                        @RequestBody ChatRoomRequest.CreateRequest request) {

        long myId = authentication.getId();
        long otherUserId = request.getOtherUserId();
        ChatRoom chattingRoom = chatRoomService.create(request.getProductId(), List.of(myId, otherUserId));

        log.info("chattingRoom.getId(): {}", chattingRoom.getId());
        log.info("chattingRoom.getProductId(): {}", chattingRoom.getProductId());
        log.info("chattingRoom.getProductImage(): {}", chattingRoom.getProductImage());
        log.info("chattingRoom.getCreatedAt(): {}", chattingRoom.getCreatedAt());
        log.info("chattingRoom.getUpdatedAt(): {}", chattingRoom.getUpdatedAt());

        return OK(CreateResponse.from(chattingRoom.getId()));
    }

    /**
     * 채팅방 목록 조회 GET /api/chat-rooms
     */
    @GetMapping
    public ApiResult<List<SimpleResponse>> getChatRooms(@AuthenticationPrincipal JwtAuthentication authentication,
                                                        Pageable pageable) {

        List<ChatRoom> chatRooms = chatRoomService.getChatRooms(authentication.getId(), pageable);

        List<SimpleResponse> simpleResponses = chatRooms.stream().map(chatRoom -> {

            Long otherPersonId = chatRoom.getOtherPersonId(authentication.getId());
            User user = userService.getUser(otherPersonId);

            String profileImage = StringUtils.isEmpty(user.getProfileUrl()) ?
                null : presignerUtils.getProfilePresignedGetUrl(user.getProfileUrl());

            String productImage = StringUtils.isEmpty(chatRoom.getProductImage()) ?
                null : presignerUtils.getProductPresignedGetUrl(chatRoom.getProductImage());

            Participant participant = participantService.getParticipant(chatRoom.getId(), user.getId());
            long totalMessageSize = chatRoomService.getChatRoomMessageSize(chatRoom.getId());
            long notReadChatCount = totalMessageSize - participant.getReadMessageSize();

            return SimpleResponse.of(chatRoom, user, profileImage, participant, productImage, notReadChatCount);

        }).collect(toList());

        return OK(simpleResponses);
    }
}
