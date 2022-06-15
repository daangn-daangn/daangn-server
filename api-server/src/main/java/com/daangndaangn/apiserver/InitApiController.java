package com.daangndaangn.apiserver;

import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/init")
@RequiredArgsConstructor
@RestController
public class InitApiController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-rooms")
    public void initChatRoom(@AuthenticationPrincipal JwtAuthentication authentication) {
        chatRoomService.create(1L, List.of(authentication.getId(), 1L));
        chatRoomService.create(2L, List.of(authentication.getId(), 2L));
        chatRoomService.create(3L, List.of(authentication.getId(), 3L));
        chatRoomService.create(4L, List.of(authentication.getId(), 4L));
        chatRoomService.create(5L, List.of(authentication.getId(), 5L));
    }

    @PostMapping("/chat-messages")
    public void initChatMessages(@RequestParam("id") String id, @AuthenticationPrincipal JwtAuthentication authentication) {

        int messageType = 1;
        int loopSize = 10;

        for (int loop = 0; loop < loopSize; ++loop) {
            chatRoomService.addChatMessage(id, authentication.getId(), messageType, String.format("메시지%d", loop));
        }
    }
}
