package com.daangndaangn.apiserver.service.chatroom;

import com.daangndaangn.apiserver.service.participant.ParticipantService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@Service
@Transactional(readOnly = true, value = "mongoTransactionManager")
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantService participantService;
    private final ProductService productService;
    private final UserService userService;

    @Override
    @Transactional(value = "mongoTransactionManager")
    public ChatRoom create(Long productId, List<Long> userIds) {
        checkArgument(productId != null, "productId 값은 필수입니다.");
        checkArgument(userIds != null, "userIds 값은 필수입니다.");
        checkArgument(userIds.size() == 2, "userIds size는 2여야 합니다.");
        checkArgument(userIds.get(0) != null && userIds.get(1) != null, "userIds 안에 값은 필수입니다.");
        checkArgument(!userIds.get(0).equals(userIds.get(1)), "두 userId는 같을 수 없습니다.");

        Long userId1 = userIds.get(0);
        Long userId2 = userIds.get(1);

        if (!userService.existById(userId1)) {
            throw new NotFoundException(User.class, String.format("id = %d", userId1));
        }

        if (!userService.existById(userId2)) {
            throw new NotFoundException(User.class, String.format("id = %d", userId2));
        }

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
        checkArgument(userId != null, "userId 값은 필수입니다.");

        List<String> chatRoomIds = participantService.getParticipants(userId, pageable)
                .stream()
                .map(Participant::getChatRoomId).collect(toList());

        return chatRoomRepository.findAllByChatRoomIds(chatRoomIds,
                Sort.by(Sort.Direction.DESC, "updated_at"));
    }

    @Override
    public ChatRoom getChatRoom(String id) {
        checkArgument(isNotEmpty(id), "id 값은 필수입니다.");

        return chatRoomRepository.findChatRoomById(id)
                .orElseThrow(() -> new NotFoundException(ChatRoom.class, String.format("id = %s", id)));
    }

    @Override
    public String toIdentifier(Long userId1, Long userId2) {
        checkArgument(userId1 != null, "userId1 값은 필수입니다.");
        checkArgument(userId2 != null, "userId2 값은 필수입니다.");

        long firstUserId = Math.min(userId1, userId2);
        long secondUserId = Math.max(userId1, userId2);

        return firstUserId + "-" + secondUserId;
    }

    @Override
    public long getChatRoomMessageSize(String id) {
        checkArgument(isNotEmpty(id), "id 값은 필수입니다.");

        return chatRoomRepository.getChatRoomMessageSize(id);
    }
}
