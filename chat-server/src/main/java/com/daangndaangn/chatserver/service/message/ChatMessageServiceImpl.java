package com.daangndaangn.chatserver.service.message;


import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.document.message.ChatMessage;
import com.daangndaangn.common.chat.document.message.MessageType;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@Service
@Transactional(readOnly = true, value = "mongoTransactionManager")
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantRepository participantRepository;

    @Override
    @Transactional(value = "mongoTransactionManager")
    public long addChatMessage(String id, Long senderId, Long receiverId, int messageTypeCode, String message) {
        checkArgument(isNotEmpty(id), "id는 null일 수 없습니다.");
        checkArgument(senderId != null, "senderId는 null일 수 없습니다.");
        checkArgument(receiverId != null, "receiverId는 null일 수 없습니다.");
        checkArgument(1 <= messageTypeCode && messageTypeCode <= 3,
                "messageTypeCode는 1,2,3 중에 하나여야 합니다.");
        checkArgument(isNotEmpty(message), "message는 null일 수 없습니다.");

        ChatMessage chatMessage = ChatMessage.builder()
                .senderId(senderId)
                .messageType(MessageType.from(messageTypeCode))
                .message(message)
                .build();

        long messageUpdateCount = chatRoomRepository.insertChatMessage(id, chatMessage);
        long participantUpdateCount = participantRepository.synchronizeUpdatedAt(id, List.of(senderId, receiverId), chatMessage.getCreatedAt());

        log.info("messageUpdateCount = {}", messageUpdateCount);
        log.info("participantUpdateCount = {}", participantUpdateCount);

        return messageUpdateCount + participantUpdateCount;
    }

    @Override
    public ChatRoom getChatRoomWithMessages(String id, int page) {
        checkArgument(isNotEmpty(id), "id는 null일 수 없습니다.");
        checkArgument(page >= 0, "page는 0보다 작을 수 없습니다.");

        final int chatMessageSize = 10;

        ChatRoom chatRoom = chatRoomRepository.findChatRoomWithChatMessages(id, page, chatMessageSize)
                .orElseThrow(() -> new NotFoundException(ChatRoom.class, String.format("id = %s", id)));

        Collections.reverse(chatRoom.getChatMessages());

        return chatRoom;
    }

    @Override
    @Transactional(value = "mongoTransactionManager")
    public void updateReadMessageSize(String id, Long userId) {
        checkArgument(isNotEmpty(id), "id는 null일 수 없습니다.");
        checkArgument(userId != null, "userId는 null일 수 없습니다.");

        Participant participant = participantRepository.findByChatRoomIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException(Participant.class,
                        String.format("id = %s, userId = %d", id, userId)));

        Long chatRoomMessageSize = chatRoomRepository.getChatRoomMessageSize(id);

        participant.update(chatRoomMessageSize);
        participantRepository.save(participant);
    }
}
