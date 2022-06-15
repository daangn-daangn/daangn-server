package com.daangndaangn.chatserver.controller;

import com.daangndaangn.chatserver.constants.KafkaConstants;
import com.daangndaangn.chatserver.controller.chatroom.ChatMessageRequest.CreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

    private final KafkaTemplate<String, CreateRequest> kafkaTemplate;

    public void send(CreateRequest message) {
        log.info("sending data='{}'", message);
        //Sending the message to kafka topic queue
        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message);
    }
}
