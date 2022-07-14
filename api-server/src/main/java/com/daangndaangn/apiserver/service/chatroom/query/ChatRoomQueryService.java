package com.daangndaangn.apiserver.service.chatroom.query;

import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    public List<User> getChatRoomBuyersByProductId(Long sellerId, Long productId, Pageable pageable) {
        checkArgument(sellerId != null, "userId 값은 필수입니다.");
        checkArgument(productId != null, "productId 값은 필수입니다.");

        List<Long> userIds = chatRoomRepository.findAllByProductId(productId, pageable)
                .stream()
                .map(chatRoom -> chatRoom.getOtherUserId(sellerId))
                .collect(toList());

        return userService.getUsers(userIds);
    }
}
