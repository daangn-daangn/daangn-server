package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.user.UserRepository;
import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.util.UploadUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UploadUtils uploadUtils;

    private User mockUser;

    @BeforeEach
    public void init() {
        mockUser = User.builder()
                .id(1L)
                .oauthId(12345L)
                .build();
    }

    @Test
    public void 유저를_생성할_수_있다() throws ExecutionException, InterruptedException {
        //given
        given(userRepository.existsByOAuth(anyLong())).willReturn(false);
        given(userRepository.save(any())).willReturn(mockUser);

        String profileImageUrl = "testProfileImageUrl";

        //when
        Long userId = userService.create(mockUser.getOauthId(), profileImageUrl).get();

        //then
        assertThat(userId).isEqualTo(mockUser.getId());

        verify(userRepository).existsByOAuth(anyLong());
        verify(userRepository, never()).findByOauthId(anyLong());
        verify(userRepository).save(any());
    }

    @Test
    public void 이미_존재하는_사용자인_경우_기존_유저를_반환한다() throws ExecutionException, InterruptedException {
        //given
        given(userRepository.existsByOAuth(anyLong())).willReturn(true);
        given(userRepository.findByOauthId(anyLong())).willReturn(Optional.ofNullable(mockUser));

        //when
        Long userId = userService.create(mockUser.getOauthId(), "testProfileImageUrl").get();

        //then
        assertThat(userId).isEqualTo(mockUser.getId());

        verify(userRepository).existsByOAuth(anyLong());
        verify(userRepository).findByOauthId(anyLong());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void 사용자는_닉네임과_위치_프로필이미지를_업데이트_할_수_있다() {
        //given
        given(userRepository.findByOauthId(anyLong())).willReturn(Optional.ofNullable(mockUser));
        given(uploadUtils.isNotImageFile(anyString())).willReturn(false);
        given(userRepository.exists(anyString())).willReturn(false);

        assertThat(mockUser.getNickname()).isNull();
        assertThat(mockUser.getLocation()).isNull();
        assertThat(mockUser.getProfileUrl()).isNull();

        String newNickname = "newNickname";
        Location newLocation = Location.from("new address");
        String newProfileUrl = "newProfileUrl";

        //when
        long updatedId = userService.update(mockUser.getOauthId(), newNickname, newLocation, newProfileUrl);

        //then
        assertThat(updatedId).isEqualTo(mockUser.getId());
        assertThat(mockUser.getNickname()).isEqualTo(newNickname);
        assertThat(mockUser.getLocation()).isEqualTo(newLocation);
        assertThat(mockUser.getProfileUrl()).isNotEmpty();

        verify(userRepository).findByOauthId(anyLong());
        verify(uploadUtils).isNotImageFile(anyString());
        verify(userRepository).exists(anyString());
    }

    @Test
    public void 프로필이미지_형식이_올바르지_않은_경우_예외를_반환한다() {
        //given
        given(userRepository.findByOauthId(anyLong())).willReturn(Optional.ofNullable(mockUser));
        given(uploadUtils.isNotImageFile(anyString())).willReturn(true);

        String newNickname = "newNickname";
        Location newLocation = Location.from("new address");
        String newProfileUrl = "newProfileUrl";

        //when
        assertThrows(IllegalArgumentException.class,
            () -> userService.update(mockUser.getOauthId(), newNickname, newLocation, newProfileUrl));

        //then
        verify(userRepository).findByOauthId(anyLong());
        verify(uploadUtils).isNotImageFile(anyString());
        verify(userRepository, never()).exists(anyString());
    }

    @Test
    public void 이미_존재하는_닉네임인_경우_예외를_반환한다() {
        //given
        given(userRepository.findByOauthId(anyLong())).willReturn(Optional.ofNullable(mockUser));
        given(uploadUtils.isNotImageFile(anyString())).willReturn(false);
        given(userRepository.exists(anyString())).willReturn(true);

        String newNickname = "newNickname";
        Location newLocation = Location.from("new address");
        String newProfileUrl = "newProfileUrl";

        //when
        assertThrows(DuplicateValueException.class,
                () -> userService.update(mockUser.getOauthId(), newNickname, newLocation, newProfileUrl));

        //then
        verify(userRepository).findByOauthId(anyLong());
        verify(uploadUtils).isNotImageFile(anyString());
        verify(userRepository).exists(anyString());
    }

    @Test
    public void 닉네임_중복여부를_확인할_수_있다() {
        //given
        String testNickname = "testNickname";
        given(userRepository.exists(anyString())).willReturn(true);

        //when
        boolean result = userService.isValidNickname(testNickname);

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void 유저는_닉네임과_사는_동네를_업데이트_할_수_있다() {
        //given
        String beforeNickname = mockUser.getNickname();
        Location beforeLocation = mockUser.getLocation();

        assertThat(beforeNickname).isNull();
        assertThat(beforeLocation).isNull();

        String newNickname = "newNickname";
        Location newLocation = Location.from("new address");
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockUser));

        //when
        userService.update(mockUser.getId(), newNickname, newLocation.getAddress());

        String afterNickname = mockUser.getNickname();
        Location afterLocation = mockUser.getLocation();

        //then
        verify(userRepository).findById(anyLong());
        assertThat(afterNickname).isNotNull();
        assertThat(afterLocation).isNotNull();
        assertThat(newNickname).isEqualTo(mockUser.getNickname());
        assertThat(newLocation).isEqualTo(afterLocation);
    }

}
