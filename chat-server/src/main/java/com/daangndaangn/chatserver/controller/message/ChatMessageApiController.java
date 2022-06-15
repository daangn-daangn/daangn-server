//package com.daangndaangn.chatserver.controller.message;
//
//import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
//import com.daangndaangn.chatserver.controller.message.ChatMessageResponse.GetResponse;
//import com.daangndaangn.chatserver.controller.MessageSender;
//import com.daangndaangn.common.web.ApiResult;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//import static com.daangndaangn.chatserver.controller.message.ChatMessageResponse.GetResponse.from;
//import static com.daangndaangn.common.web.ApiResult.OK;
//import static java.util.stream.Collectors.toList;
//import static org.apache.commons.lang3.StringUtils.isNotEmpty;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//public class ChatMessageApiController {
//
//    private final ChatMessageService chatMessageService;
//    private final MessageSender messageSender;
//
//    /**
//     * POST /chat/messages
//     */
//    @PostMapping("/chat/messages")
//    public void sendMessage(@RequestBody CreateRequest message) {
//        log.info("call sendMessage");
//        String chatMessageId = chatMessageService.create(message.getRoomId(),
//                                                         message.getSenderId(),
//                                                         message.getMessageType(),
//                                                         message.getMessage());
//
//        if (isNotEmpty(chatMessageId)) {
//            messageSender.send(message);
//        }
//    }
//
//    /**
//     * GET /chat/messages?room_id=1
//     */
//    @GetMapping("/chat/messages")
//    public ApiResult<List<GetResponse>> getChatMessages(@RequestParam("room_id") String roomId,
//                                                        @PageableDefault(
//                                                            size = 15,
//                                                            sort = "id",
//                                                            direction = Sort.Direction.DESC) Pageable pageable) {
//
//        return OK(chatMessageService.getChatMessages(roomId, pageable)
//                .stream()
//                .map(chatMessage -> from(chatMessage))
//                .collect(toList()));
//    }
//}
