package com.daangndaangn.chatserver.service;

import com.daangndaangn.common.chat.document.message.ChattingMessage;
//import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.common.chat.repository.message.ChattingMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;
    public ChattingMessage findChattingMessage(String roomId){
        //NotFoundException을 import 할 수 없다고 나와서 임시로 처리했습니다.
//        return chattingMessageRepository.findById(roomId).orElseThrow(() -> new NotFoundException(ChattingMessage.class, String.format("roomId = %s", 0)))
        return chattingMessageRepository.findById(roomId).get();
    }
    public void createMessage(String message, Long userId, String roomId){
        ChattingMessage chattingMessage = this.findChattingMessage(roomId);
        chattingMessage.addMessage(userId, message);
        chattingMessageRepository.save(chattingMessage);
    }
}
