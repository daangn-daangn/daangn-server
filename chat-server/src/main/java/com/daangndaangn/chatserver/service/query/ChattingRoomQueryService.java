package com.daangndaangn.chatserver.service.query;

import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.UserRepository;
import com.daangndaangn.common.chat.document.ChattingRoom;
import com.daangndaangn.common.chat.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChattingRoomQueryService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;

    /**
     *  private String profileImage;
     *  private String nickname;
     *  private String location;
     *  private String productThumbNailImage;
     *  private String lastChat;
     *  private LocalDateTime createdAt;
     */
    public List<ChattingRoomQueryDto> getChattingRoomQueryDtos(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId must not be null");

        List<ChattingRoom> chattingRooms = chattingRoomRepository.findAllByFirstUserIdOrSecondUserId(userId, userId, pageable);

        Set<Long> userIdSet = new HashSet<>();

        for (ChattingRoom chattingRoom : chattingRooms) {
            userIdSet.add(chattingRoom.getFirstUserId());
            userIdSet.add(chattingRoom.getSecondUserId());
        }

        List<User> users = userRepository.findAllByIdIn(new ArrayList<>(userIdSet));
        Map<Long, User> userMap = toUserMap(users);

        return chattingRooms.stream().map(chattingRoom -> {
            Long firstUserId = chattingRoom.getFirstUserId();
            Long secondUserId = chattingRoom.getSecondUserId();
            Long targetUserId = userId.equals(firstUserId) ? firstUserId : secondUserId;
            User user = userMap.get(targetUserId);

            return ChattingRoomQueryDto.of(chattingRoom, user);
        }).collect(toList());
    }

    private Map<Long, User> toUserMap(List<User> users) {
        Map<Long, User> userMap = new HashMap<>();

        for (User user : users) {
            userMap.put(user.getId(), user);
        }

        return userMap;
    }
}
