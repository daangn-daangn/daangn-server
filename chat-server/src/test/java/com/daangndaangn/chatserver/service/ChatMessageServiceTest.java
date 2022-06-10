package com.daangndaangn.chatserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @BeforeEach
    public void init() {

    }
}