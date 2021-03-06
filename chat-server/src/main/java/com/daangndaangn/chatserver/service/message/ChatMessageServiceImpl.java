package com.daangndaangn.chatserver.service.message;


import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.document.message.ChatMessage;
import com.daangndaangn.common.chat.document.message.MessageType;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.util.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.*;

@Service
@Transactional(readOnly = true, value = "mongoTransactionManager")
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantRepository participantRepository;
    private final UploadUtils uploadUtils;

    @Async
    @Override
    @Transactional(value = "mongoTransactionManager")
    public CompletableFuture<Long> addChatMessage(String id,
                                                  Long senderId,
                                                  Long receiverId,
                                                  int messageTypeCode,
                                                  String message,
                                                  List<String> imgUrls) {

        checkArgument(isNotEmpty(id), "id 값은 필수입니다.");
        checkArgument(senderId != null, "senderId 값은 필수입니다.");
        checkArgument(receiverId != null, "receiverId 값은 필수입니다.");
        checkArgument(1 <= messageTypeCode && messageTypeCode <= 3,
                "messageTypeCode 값은 1,2,3 중에 하나여야 합니다.");

        MessageType messageType = MessageType.from(messageTypeCode);
        checkValidation(messageType, message, imgUrls);

        ChatMessage chatMessage = messageType.equals(MessageType.IMAGE) ?
                ChatMessage.ofImage(senderId, imgUrls) : ChatMessage.ofMessage(senderId, messageType, message);

        long messageUpdateCount = chatRoomRepository.insertChatMessage(id, chatMessage);
        long participantUpdateCount = participantRepository.synchronizeUpdatedAt(id,
                                                                                List.of(senderId, receiverId),
                                                                                chatMessage.getCreatedAt());

        return completedFuture(messageUpdateCount + participantUpdateCount);
    }

    private void checkValidation(MessageType messageType, String message, List<String> imgUrls) {
        switch (messageType) {
            case POSITION:
                checkArgument(isNotBlank(message), "message 값은 필수입니다.");
                checkArgument(isEmpty(imgUrls), "좌표 요청 시 imgUrls 값은 없어야 합니다.");

                String[] components = message.split(",");
                if (components.length != 2) {
                    throw new IllegalArgumentException("올바른 좌표 메시지 형식이 아닙니다.");
                }
                break;
            case IMAGE:
                checkArgument(isBlank(message), "이미지 요청 시 message 값은 없어야 합니다.");
                checkArgument(isNotEmpty(imgUrls), "imgUrls 값은 필수입니다.");

                boolean isInvalid = imgUrls.stream().anyMatch(uploadUtils::isNotImageFile);

                if (isInvalid) {
                    throw new IllegalArgumentException("올바른 이미지 메시지 형식이 아닙니다.");
                }
                break;
            default:
                checkArgument(isNotBlank(message), "message 값은 필수입니다.");
                checkArgument(isEmpty(imgUrls), "좌표 요청 시 imgUrls 값은 없어야 합니다.");
        }
    }

    @Override
    public ChatRoom getChatRoomWithMessages(String id, int page) {
        checkArgument(isNotEmpty(id), "id 값은 필수입니다.");
        checkArgument(page >= 0, "page는 0보다 커야합니다.");

        final int chatMessageSize = 10;

        ChatRoom chatRoom = chatRoomRepository.findChatRoomWithChatMessages(id, page, chatMessageSize)
                .orElseThrow(() -> new NotFoundException(ChatRoom.class, String.format("id = %s", id)));

        Collections.reverse(chatRoom.getChatMessages());

        return chatRoom;
    }

    @Override
    @Transactional(value = "mongoTransactionManager")
    public void updateReadMessageSize(String id, Long userId) {
        checkArgument(isNotEmpty(id), "id 값은 필수입니다.");
        checkArgument(userId != null, "userId 값은 필수입니다.");

        Participant participant = participantRepository.findByChatRoomIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException(Participant.class,
                        String.format("id = %s, userId = %d", id, userId)));

        Long chatRoomMessageSize = chatRoomRepository.getChatRoomMessageSize(id);

        participant.update(chatRoomMessageSize);
        participantRepository.save(participant);
    }
}
