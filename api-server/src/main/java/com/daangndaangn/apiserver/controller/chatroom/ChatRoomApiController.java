package com.daangndaangn.apiserver.controller.chatroom;

import com.daangndaangn.apiserver.controller.chatroom.ChatRoomResponse.CreateResponse;
import com.daangndaangn.apiserver.controller.chatroom.ChatRoomResponse.DetailResponse;
import com.daangndaangn.apiserver.controller.chatroom.ChatRoomResponse.SimpleResponse;
import com.daangndaangn.apiserver.service.chatroom.ChatRoomService;
import com.daangndaangn.apiserver.service.participant.ParticipantService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.jwt.JwtAuthentication;
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
    private final ProductService productService;
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

        final int MESSAGE_PAGE_SIZE = 10;

        List<ChatRoom> chatRooms = chatRoomService.getChatRooms(authentication.getId(), pageable);

        List<SimpleResponse> simpleResponses = chatRooms.stream().map(chatRoom -> {

            Long otherUserId = chatRoom.getOtherUserId(authentication.getId());
            User user = userService.getUser(otherUserId);

            String profileImage = StringUtils.isEmpty(user.getProfileUrl()) ?
                null : presignerUtils.getProfilePresignedGetUrl(user.getProfileUrl());

            String productImage = StringUtils.isEmpty(chatRoom.getProductImage()) ?
                null : presignerUtils.getProductPresignedGetUrl(chatRoom.getProductImage());

            Participant participant = participantService.getParticipant(chatRoom.getId(), user.getId());
            long totalMessageSize = chatRoomService.getChatRoomMessageSize(chatRoom.getId());
            long messagePageOffset = Math.max(0, totalMessageSize - MESSAGE_PAGE_SIZE);
            long notReadChatCount = totalMessageSize - participant.getReadMessageSize();

            return SimpleResponse.of(chatRoom,
                                    user,
                                    profileImage,
                                    messagePageOffset,
                                    MESSAGE_PAGE_SIZE,
                                    productImage,
                                    notReadChatCount);
        }).collect(toList());

        return OK(simpleResponses);
    }

    /**
     * 채팅방 상세 정보 조회
     *
     * GET /api/chat-rooms/:chat-room-id
     */
    @GetMapping("/{chatRoomId}")
    public ApiResult<DetailResponse> getChatRoom(@AuthenticationPrincipal JwtAuthentication authentication,
                                                 @PathVariable("chatRoomId") String chatRoomId) {

        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        Long otherUserId = chatRoom.getOtherUserId(authentication.getId());
        User otherUser = userService.getUser(otherUserId);
        Product product = productService.getProduct(chatRoom.getProductId());
        Participant participant = participantService.getParticipant(chatRoomId, otherUserId);

        String profileImage = StringUtils.isEmpty(otherUser.getProfileUrl()) ?
                null : presignerUtils.getProfilePresignedGetUrl(otherUser.getProfileUrl());

        String productImage = StringUtils.isEmpty(product.getThumbNailImage()) ?
                null : presignerUtils.getProductPresignedGetUrl(product.getThumbNailImage());

        return OK(DetailResponse.of(chatRoom, participant.isOut(), otherUser, profileImage, product, productImage));
    }
}
