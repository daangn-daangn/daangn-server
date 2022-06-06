package com.daangndaangn.chatserver.controller.room;

import com.daangndaangn.chatserver.controller.room.RoomResponse.CreateResponse;
import com.daangndaangn.chatserver.service.ChattingRoomService;
import com.daangndaangn.common.chat.document.ChattingRoom;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/chat/chatting-rooms")
@RestController
@RequiredArgsConstructor
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;

//    @GetMapping
//    public ApiResult<Void> func(@AuthenticationPrincipal) {
//    }

    @GetMapping("/{userId}")
    public void func2() {

    }

    @PostMapping
    public ApiResult<CreateResponse> createChattingRoom(@RequestBody RoomRequest.CreateRequest request) {
        ChattingRoom chattingRoom = chattingRoomService.create(request.getProductId(), request.getUserIds());

        log.info("chattingRoom.getId(): {}", chattingRoom.getId());
        log.info("chattingRoom.getProductId(): {}", chattingRoom.getProductId());
        log.info("chattingRoom.getProductImage(): {}", chattingRoom.getProductImage());
        log.info("chattingRoom.getCreatedAt(): {}", chattingRoom.getCreatedAt());
        log.info("chattingRoom.getUpdatedAt(): {}", chattingRoom.getUpdatedAt());

        return ApiResult.OK(CreateResponse.from(chattingRoom.getId()));
    }
}
