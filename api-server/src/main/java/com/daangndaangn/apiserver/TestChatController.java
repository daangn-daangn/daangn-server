package com.daangndaangn.apiserver;

import com.daangndaangn.apiserver.service.chatroom.ChatRoomService;
import com.daangndaangn.apiserver.service.chatroom.query.ChatRoomQueryDto;
import com.daangndaangn.apiserver.service.chatroom.query.ChatRoomQueryService;
import com.daangndaangn.apiserver.service.participant.ParticipantService;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api/chat/test")
@RequiredArgsConstructor
@RestController
public class TestChatController {

    private final ParticipantService participantService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomQueryService chatRoomQueryService;

    /**
     * /api/chat/test/messages
     */
    @PostMapping("/messages")
    public void addMessage(@RequestParam("id") String id, @RequestBody ChatMessageDto chatMessageDto) {
        chatRoomService.addChatMessage(id,
                                        chatMessageDto.getSenderId(),
                                        chatMessageDto.getMessageType(),
                                        chatMessageDto.getMessage());
    }

    @GetMapping("/v2/messages")
    public void getMessagesV2(@RequestParam("id") String id, @RequestParam("page") Integer page) {
        chatRoomService.getChatRoomWithMessages(id, page);
    }



    @GetMapping("/chatting-rooms/v1/{userId}")
    public List<ChattingRoomDto> getChattingRooms(@PathVariable("userId") Long userId, Pageable pageable) {
        List<ChatRoom> chattingRooms = chatRoomService.getChatRooms(userId, pageable);

        return chattingRooms.stream()
                .map(ChattingRoomDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/chatting-rooms/v2/{userId}")
    public List<ChatRoomQueryDto> getChattingRoomQueryDtos(@PathVariable("userId") Long userId, Pageable pageable) {
        List<ChatRoomQueryDto> queryDtos = chatRoomQueryService.getChattingRoomQueryDtos(userId, pageable);
        return queryDtos;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChattingRoomDto {
        private Long productId;
        private Long userId1;
        private Long userId2;

        public static ChattingRoomDto from(ChatRoom chattingRoom) {
            return ChattingRoomDto.builder()
                    .productId(chattingRoom.getProductId())
                    .userId1(chattingRoom.getFirstUserId())
                    .userId2(chattingRoom.getSecondUserId())
                    .build();
        }
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
