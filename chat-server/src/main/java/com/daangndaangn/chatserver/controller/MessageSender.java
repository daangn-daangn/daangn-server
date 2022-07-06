package com.daangndaangn.chatserver.controller;

import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

    private final SimpMessagingTemplate template;

    public void send(CreateRequest message) {
        //Sending the message to kafka topic queue
        template.convertAndSend("/topic/chat/" + message.getRoomId(), message);
    }
}
