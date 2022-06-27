package com.daangndaangn.chatserver.controller;

import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
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
        topics = "${spring.kafka.topic.chat-message}"
    )
    public void listen(CreateRequest message) {
        log.info("sending via kafka listener..");
        template.convertAndSend("/topic/chat/" + message.getRoomId(), message);
    }
}
