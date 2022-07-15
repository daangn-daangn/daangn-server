package com.daangndaangn.apiserver.service.chatroom.query;

import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatRoomQueryServiceTest {

    @InjectMocks
    private ChatRoomQueryService chatRoomQueryService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserService userService;

    private User mockUser;
    private Product mockProduct;

    @BeforeEach
    public void init() {
        Category mockCategory = Category.builder().id(1L).name("mockCategory").build();
        mockUser = User.builder()
                .id(1L)
                .oauthId(12345L)
                .build();

        mockUser.update("testNickname", Location.from("test address"));

        mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser)
                .category(mockCategory)
                .title("테스트 title 입니다.")
                .price(1L)
                .description("테스트 description 입니다.")
                .build();
    }

    @Test
    @DisplayName("판매자는_자신이_파는_product에_대해_개설되어있는_채팅의_User_목록을_조회할_수_있다")
    public void getChatRoomBuyersByProductId() {
        //given
        List<User> mockUsers = new LinkedList<>();
        List<ChatRoom> mockChatRooms = new LinkedList<>();

        for (int i = 2; i < 7; ++i) {

            User user = User.builder()
                    .id((long) i)
                    .oauthId((long) i)
                    .build();

            mockUsers.add(user);

            ChatRoom chatRoom = ChatRoom.builder()
                    .id("testChatRoomId" + i)
                    .productId(mockProduct.getId())
                    .productImage(null)
                    .firstUserId(mockUser.getId())
                    .secondUserId((long) i)
                    .identifier(String.format("%d-%d", mockUser.getId(), (long) i))
                    .build();

            mockChatRooms.add(chatRoom);
        }

        given(chatRoomRepository.findAllByProductId(anyLong(), any())).willReturn(mockChatRooms);
        given(userService.getUsers(any())).willReturn(mockUsers);

        //when
        List<User> result = chatRoomQueryService.getChatRoomBuyersByProductId(mockUser.getId(),
                                                                                mockProduct.getId(),
                                                                                PageRequest.of(0, 5));

        //then
        assertThat(result.size()).isEqualTo(mockUsers.size());
        verify(chatRoomRepository).findAllByProductId(anyLong(), any());
        verify(userService).getUsers(any());
    }
}