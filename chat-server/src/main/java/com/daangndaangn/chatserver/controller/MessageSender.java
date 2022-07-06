package com.daangndaangn.chatserver.controller;

import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

    private final SimpMessagingTemplate template;

    public void send(CreateRequest message) {
        try {
            template.convertAndSend("/topic/chat/" + message.getRoomId(), message);
        } catch (MessagingException e) {
            log.warn("MessageSender send error occurred with roomId: {}", message.getRoomId(), e);
        }
    }
}
