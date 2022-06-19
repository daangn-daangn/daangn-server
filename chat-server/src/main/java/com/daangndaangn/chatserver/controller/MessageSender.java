package com.daangndaangn.chatserver.controller;

import com.daangndaangn.chatserver.controller.message.ChatMessageRequest.CreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

    @Value("${spring.kafka.topic.chat-message}")
    private String kafkaTopic;

    private final KafkaTemplate<String, CreateRequest> kafkaTemplate;

    public void send(CreateRequest message) {
        log.info("sending data='{}' to topic='{}'", message, kafkaTopic);
        //Sending the message to kafka topic queue
        kafkaTemplate.send(kafkaTopic, message);
    }
}
