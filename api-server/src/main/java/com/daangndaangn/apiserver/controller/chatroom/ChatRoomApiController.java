package com.daangndaangn.apiserver.controller.chatroom;

import com.daangndaangn.apiserver.controller.chatroom.ChatRoomResponse.CreateResponse;
import com.daangndaangn.apiserver.service.chatroom.ChatRoomService;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/chat-rooms")
@RestController
@RequiredArgsConstructor
public class ChatRoomApiController {

    private final ChatRoomService chattingRoomService;

    @PostMapping
    public ApiResult<CreateResponse> createChattingRoom(@RequestBody ChatRoomRequest.CreateRequest request) {
        ChatRoom chattingRoom = chattingRoomService.create(request.getProductId(), request.getUserIds());

        log.info("chattingRoom.getId(): {}", chattingRoom.getId());
        log.info("chattingRoom.getProductId(): {}", chattingRoom.getProductId());
        log.info("chattingRoom.getProductImage(): {}", chattingRoom.getProductImage());
        log.info("chattingRoom.getCreatedAt(): {}", chattingRoom.getCreatedAt());
        log.info("chattingRoom.getUpdatedAt(): {}", chattingRoom.getUpdatedAt());

        return ApiResult.OK(CreateResponse.from(chattingRoom.getId()));
    }
}
