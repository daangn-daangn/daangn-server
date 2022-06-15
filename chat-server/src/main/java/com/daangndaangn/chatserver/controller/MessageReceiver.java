package com.daangndaangn.chatserver.controller;

import com.daangndaangn.chatserver.constants.KafkaConstants;
import com.daangndaangn.chatserver.controller.message.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageReceiver {

    private final SimpMessagingTemplate template;

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(ChatMessageRequest.CreateRequest message) {
        log.info("sending via kafka listener..");
        template.convertAndSend("/topic/chat/" + message.getRoomId(), message);
    }
}
