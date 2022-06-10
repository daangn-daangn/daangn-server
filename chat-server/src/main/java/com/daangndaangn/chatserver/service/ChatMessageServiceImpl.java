package com.daangndaangn.chatserver.service;

import com.daangndaangn.common.chat.document.message.ChatMessage;
import com.daangndaangn.common.chat.document.message.MessageType;
import com.daangndaangn.common.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public String create(String roomId, Long senderId, Integer messageTypeCode, String message) {
        checkArgument(roomId != null, "roomId값은 null 일 수 없습니다.");
        checkArgument(senderId != null, "senderId값은 null 일 수 없습니다.");
        checkArgument(messageTypeCode != null, "messageTypeCode값은 null 일 수 없습니다.");
        checkArgument(message != null, "message값은 null 일 수 없습니다.");

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .messageType(MessageType.from(messageTypeCode))
                .message(message)
                .build();

        return chatMessageRepository.save(chatMessage).getId();
    }

    @Override
    public List<ChatMessage> getChatMessages(String roomId, Pageable pageable) {
        checkArgument(roomId != null, "roomId값은 null 일 수 없습니다.");
        return chatMessageRepository.findAllByRoomId(roomId, pageable);
    }
}
