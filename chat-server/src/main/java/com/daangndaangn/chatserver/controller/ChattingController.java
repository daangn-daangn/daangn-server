package com.daangndaangn.chatserver.controller;


import com.daangndaangn.chatserver.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChattingController {

    //메세지 전송을 담당하는 객체
    private final SimpMessageSendingOperations sendingOperations;
    private final ChattingService chattingService;
    //메세지를 보낼 때 사용되는 함수
    @MessageMapping("/send-message")
//    @SendTo() : convertAndSend 메소드의 첫번째 인자인 경로를 설정할 수 있다는대, 활용법 공부 필요
    public void sendMessage(String message, Long userId, String roomId){

        //브로커가 /topic/방번호를 구독하고 있는 클라이언트에게 message를 전송합니다.
        sendingOperations.convertAndSend("/topic/방번호", message);
    }
}
