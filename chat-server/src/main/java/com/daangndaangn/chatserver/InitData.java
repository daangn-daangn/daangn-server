package com.daangndaangn.chatserver;

import com.daangndaangn.chatserver.service.ChatMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {

    public static final String ROOM_ID = "62a040c660c94648d2115371";
    private final InitDataService initDataService;

    @PostConstruct
    public void init() {
        log.info("start initTestSetting");
        initDataService.initChatMessages();
        log.info("end initTestSetting");
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitDataService {

        private final ChatMessageServiceImpl chatMessageService;

        public void initChatMessages() {
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요1");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요2");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요3");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요4");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요5");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요6");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요7");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요8");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요9");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요10");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요11");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요12");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요13");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요14");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요15");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요16");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요17");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요18");
            chatMessageService.create(ROOM_ID, 2L, 1, "안녕하세요19");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요20");
            chatMessageService.create(ROOM_ID, 1L, 1, "안녕하세요21");
        }
    }
}
