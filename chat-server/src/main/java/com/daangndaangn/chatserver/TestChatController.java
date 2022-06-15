package com.daangndaangn.chatserver;

import com.daangndaangn.chatserver.service.chatroom.ChatMessageService;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/chat/test")
@RequiredArgsConstructor
@RestController
public class TestChatController {

    private final ChatMessageService chatMessageService;

    /**
     * /api/chat/test/messages
     */
    @PostMapping("/messages")
    public void addMessage(@RequestParam("id") String id, @RequestBody ChatMessageDto chatMessageDto) {
        chatMessageService.addChatMessage(id,
                    chatMessageDto.getSenderId(),
                    chatMessageDto.getMessageType(),
                    chatMessageDto.getMessage());
    }

    @GetMapping("/v2/messages")
    public void getMessagesV2(@RequestParam("id") String id, @RequestParam("page") Integer page) {
        chatMessageService.getChatRoomWithMessages(id, page);
    }

    /**
     * Long senderId, MessageType messageType, String message
     */
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatMessageDto {
        Long senderId;
        Integer messageType;
        String message;
    }
}
