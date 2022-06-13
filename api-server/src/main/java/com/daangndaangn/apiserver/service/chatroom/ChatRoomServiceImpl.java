package com.daangndaangn.apiserver.service.chatroom;

import com.daangndaangn.apiserver.service.participant.ParticipantService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.repository.ChatRoomRepository;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantService participantService;
    private final ProductRepository productRepository;

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

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, String.format("productId = %s", productId)));

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

        participantService.create(userId1, savedChattingRoom);
        participantService.create(userId2, savedChattingRoom);

        return savedChattingRoom;
    }

    @Override
    public List<ChatRoom> getChattingRooms(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId는 null일 수 없습니다.");
        return chatRoomRepository.findAllByFirstUserIdOrSecondUserId(userId, userId, pageable);
    }

    @Override
    public ChatRoom getChattingRoom(String id) {
        return chatRoomRepository.findById(id)
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
}
