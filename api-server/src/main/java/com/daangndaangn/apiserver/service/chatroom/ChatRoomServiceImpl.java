package com.daangndaangn.apiserver.service.chatroom;

import com.daangndaangn.apiserver.service.participant.ParticipantService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.message.ChatMessage;
import com.daangndaangn.common.chat.document.message.MessageType;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantService participantService;
    private final ProductService productService;

    @Override
    @Transactional(value = "mongoTransactionManager")
    public ChatRoom create(Long productId, List<Long> userIds) {
        checkArgument(productId != null, "productId는 null일 수 없습니다.");
        checkArgument(userIds != null, "userIds는 null일 수 없습니다.");
        checkArgument(userIds.size() == 2, "userIds size는 2여야 합니다.");
        checkArgument(userIds.get(0) != null && userIds.get(1) != null, "userIds 내 값은 null일 수 없습니다.");
        checkArgument(!userIds.get(0).equals(userIds.get(1)), "두 userId는 같을 수 없습니다.");

        Long userId1 = userIds.get(0);
        Long userId2 = userIds.get(1);

        String identifier = toIdentifier(userId1, userId2);

        if (chatRoomRepository.existsByProductIdAndIdentifier(productId, identifier)) {
            return chatRoomRepository.findByProductIdAndIdentifier(productId, identifier)
                    .orElseThrow(() -> new NotFoundException(ChatRoom.class,
                            String.format("productId = %s, identifier = %s", productId, identifier)));
        }

        Product product = productService.getProduct(productId);

        String productImage = product.getThumbNailImage();

        long firstUserId = Math.min(userId1, userId2);
        long secondUserId = Math.max(userId1, userId2);

        ChatRoom chattingRoom = ChatRoom.builder()
                .productId(productId)
                .productImage(productImage)
                .firstUserId(firstUserId)
                .secondUserId(secondUserId)
                .identifier(identifier)
                .build();

        ChatRoom savedChattingRoom = chatRoomRepository.save(chattingRoom);

        participantService.create(userId1, savedChattingRoom.getId());
        participantService.create(userId2, savedChattingRoom.getId());

        return savedChattingRoom;
    }

    @Override
    public List<ChatRoom> getChatRooms(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId는 null일 수 없습니다.");
        return chatRoomRepository.findAllByFirstUserIdOrSecondUserId(userId, userId, pageable);
    }

    @Override
    public ChatRoom getChatRoomWithMessages(String id, int page) {
        checkArgument(isNotEmpty(id), "id는 null일 수 없습니다.");

        final int chatMessageSize = 10;

        ChatRoom chatRoom = chatRoomRepository.findChatRoomWithChatMessages(id, page, chatMessageSize)
                .orElseThrow(() -> new NotFoundException(ChatRoom.class, String.format("id = %s", id)));

        Collections.reverse(chatRoom.getChatMessages());

        log.info("chatRoom: {}", chatRoom);

        return chatRoom;
    }

    @Override
    public ChatRoom getChatRoom(String id) {
        checkArgument(isNotEmpty(id), "id는 null일 수 없습니다.");

        return chatRoomRepository.findChatRoomById(id)
                .orElseThrow(() -> new NotFoundException(ChatRoom.class, String.format("id = %s", id)));
    }

    @Override
    public String toIdentifier(Long userId1, Long userId2) {
        checkArgument(userId1 != null, "userId1은 null일 수 없습니다.");
        checkArgument(userId2 != null, "userId2는 null일 수 없습니다.");

        long firstUserId = Math.min(userId1, userId2);
        long secondUserId = Math.max(userId1, userId2);

        return firstUserId + "-" + secondUserId;
    }

    @Override
    @Transactional(value = "mongoTransactionManager")
    public long addChatMessage(String id, Long senderId, int messageTypeCode, String message) {
        checkArgument(isNotEmpty(id), "id는 null일 수 없습니다.");
        checkArgument(senderId != null, "senderId는 null일 수 없습니다.");
        checkArgument(1 <= messageTypeCode && messageTypeCode <= 3,
                "messageTypeCode는 1,2,3 중에 하나여야 합니다.");
        checkArgument(isNotEmpty(message), "message는 null일 수 없습니다.");

        ChatMessage chatMessage = ChatMessage.builder()
                .senderId(senderId)
                .messageType(MessageType.from(messageTypeCode))
                .message(message)
                .build();

        return chatRoomRepository.insertChatMessage(id, chatMessage);
    }

    @Override
    public long getChatRoomMessageSize(String id) {
        checkArgument(isNotEmpty(id), "id는 null일 수 없습니다.");

        return chatRoomRepository.getChatRoomMessageSize(id);
    }
}
