package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.user.UserRepository;
import com.daangndaangn.common.api.repository.user.query.UserQueryDto;
import com.daangndaangn.common.api.repository.user.query.UserQueryRepository;
import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.util.UploadUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQueryRepository userQueryRepository;

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
    @DisplayName("유저를_생성할_수_있다")
    public void create1() throws ExecutionException, InterruptedException {
        //given
        given(userRepository.existsByOAuth(anyLong())).willReturn(false);
        given(userRepository.save(any())).willReturn(mockUser);

        //when
        Long userId = userService.create(mockUser.getOauthId()).get();

        //then
        assertThat(userId).isEqualTo(mockUser.getId());

        verify(userRepository).existsByOAuth(anyLong());
        verify(userRepository, never()).findByOauthId(anyLong());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("이미_존재하는_사용자인_경우_기존_유저를_반환한다")
    public void create2() throws ExecutionException, InterruptedException {
        //given
        given(userRepository.existsByOAuth(anyLong())).willReturn(true);
        given(userRepository.findByOauthId(anyLong())).willReturn(Optional.ofNullable(mockUser));

        //when
        Long userId = userService.create(mockUser.getOauthId()).get();

        //then
        assertThat(userId).isEqualTo(mockUser.getId());

        verify(userRepository).existsByOAuth(anyLong());
        verify(userRepository).findByOauthId(anyLong());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("사용자는_닉네임과_위치_프로필이미지를_업데이트_할_수_있다")
    public void update1() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockUser));
        given(uploadUtils.isNotImageFile(anyString())).willReturn(false);
        given(userRepository.exists(anyString())).willReturn(false);

        assertThat(mockUser.getNickname()).isNull();
        assertThat(mockUser.getLocation()).isNull();
        assertThat(mockUser.getProfileUrl()).isNull();

        String newNickname = "newNickname";
        Location newLocation = Location.from("new address");
        String newProfileUrl = "newProfileUrl";

        //when
        long updatedId = userService.update(mockUser.getId(), newNickname, newLocation, newProfileUrl);

        //then
        assertThat(updatedId).isEqualTo(mockUser.getId());
        assertThat(mockUser.getNickname()).isEqualTo(newNickname);
        assertThat(mockUser.getLocation()).isEqualTo(newLocation);
        assertThat(mockUser.getProfileUrl()).isNotEmpty();

        verify(userRepository).findById(anyLong());
        verify(uploadUtils).isNotImageFile(anyString());
        verify(userRepository).exists(anyString());
    }

    @Test
    @DisplayName("프로필이미지_형식이_올바르지_않은_경우_예외를_반환한다")
    public void update2() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockUser));
        given(uploadUtils.isNotImageFile(anyString())).willReturn(true);

        String newNickname = "newNickname";
        Location newLocation = Location.from("new address");
        String newProfileUrl = "newProfileUrl";

        //when
        assertThrows(IllegalArgumentException.class,
            () -> userService.update(mockUser.getOauthId(), newNickname, newLocation, newProfileUrl));

        //then
        verify(userRepository).findById(anyLong());
        verify(uploadUtils).isNotImageFile(anyString());
        verify(userRepository, never()).exists(anyString());
    }

    @Test
    @DisplayName("이미_존재하는_닉네임인_경우_예외를_반환한다")
    public void update3() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockUser));
        given(uploadUtils.isNotImageFile(anyString())).willReturn(false);
        given(userRepository.exists(anyString())).willReturn(true);

        String newNickname = "newNickname";
        Location newLocation = Location.from("new address");
        String newProfileUrl = "newProfileUrl";

        //when
        assertThrows(DuplicateValueException.class,
                () -> userService.update(mockUser.getId(), newNickname, newLocation, newProfileUrl));

        //then
        verify(userRepository).findById(anyLong());
        verify(uploadUtils).isNotImageFile(anyString());
        verify(userRepository).exists(anyString());
    }

    @Test
    @DisplayName("유저는_닉네임과_사는_동네를_업데이트_할_수_있다")
    public void update4() {
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

    @Test
    @DisplayName("user Id 리스트를 사용해서 사용자들을 조회할 수 있다")
    public void getUsers() {
        //given
        List<User> users = Arrays.asList(mockUser, mockUser, mockUser);
        List<Long> userIds = Arrays.asList(mockUser.getId(), mockUser.getId(), mockUser.getId());

        given(userRepository.findAll(anyList())).willReturn(users);

        //when
        List<User> result = userService.getUsers(userIds);

        //then
        assertThat(result.size()).isEqualTo(users.size());
        verify(userRepository).findAll(anyList());
    }

    @Test
    @DisplayName("사용자를 삭제할 수 있다")
    public void delete() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockUser));

        //when
        userService.delete(mockUser.getId());

        //then
        verify(userRepository).findById(anyLong());
        verify(userRepository).delete(any());
    }

    @Test
    @DisplayName("존재하지_않고_유효성에_맞는_닉네임은_true를_반환한다")
    public void isValidNickname1() {
        //given
        String validNewNickname = "testNickname";
        given(userRepository.exists(anyString())).willReturn(false);

        //when
        boolean result = userService.isValidNickname(validNewNickname);

        //then
        assertThat(result).isEqualTo(true);
        verify(userRepository).exists(anyString());
    }

    @Test
    @DisplayName("닉네임_중복여부를_확인할_수_있다")
    public void isValidNickname2() {
        //given
        String duplicatedNickname = "testNickname";
        given(userRepository.exists(anyString())).willReturn(true);

        //when
        boolean result = userService.isValidNickname(duplicatedNickname);

        //then
        assertThat(result).isEqualTo(false);
        verify(userRepository).exists(anyString());
    }

    @Test
    @DisplayName("올바르지 않은 형식의 닉네임은 닉네임 유효성 확인 시 false를 반환한다")
    public void isValidNickname3() {
        //given
        String invalidNickname = "testNicknametestNicknametestNicknametestNicknametestNickname";

        //when
        boolean result = userService.isValidNickname(invalidNickname);

        //then
        assertThat(result).isEqualTo(false);
        verify(userRepository, never()).exists(anyString());
    }

    @Test
    @DisplayName("올바르지 않은 형식의 닉네임은 닉네임 유효성 확인 시 false를 반환한다")
    public void isValidNickname4() {
        //given
        String emptyNickname = "";

        //when
        boolean result = userService.isValidNickname(emptyNickname);

        //then
        assertThat(result).isEqualTo(false);
        verify(userRepository, never()).exists(anyString());
    }

    /**
     * test existById
     */
    @Test
    @DisplayName("존재하는 회원인지 조회할 수 있다")
    public void existById1() {
        //given
        given(userRepository.exists(anyLong())).willReturn(true);

        //when
        boolean result = userService.existById(mockUser.getId());

        //then
        assertThat(result).isTrue();
        verify(userRepository).exists(anyLong());
    }

    @Test
    @DisplayName("id가 null이면 예외를 반환한다")
    public void existById2() {
        //given
        Long invalidUserId = null;
        //when
        assertThatThrownBy(() -> userService.existById(invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(userRepository, never()).exists(anyLong());
    }

    @Test
    @DisplayName("사용자와 매너평가점수별 평가 인원 합계를 조회할 수 있다")
    public void getUserMannerEvaluations1() {
        //given
        List<UserQueryDto> queryDtos = new LinkedList<>();
        for (int loop = 1; loop <= 5; ++loop) {
            queryDtos.add(new UserQueryDto(mockUser.getId(), loop, (long)loop));
        }

        given(userRepository.exists(anyLong())).willReturn(true);
        given(userQueryRepository.findAll(anyLong())).willReturn(queryDtos);

        //when
        List<UserQueryDto> result = userService.getUserMannerEvaluations(mockUser.getId());


        //then
        assertThat(result.size()).isEqualTo(queryDtos.size());
        verify(userRepository).exists(anyLong());
        verify(userQueryRepository).findAll(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 회원은 사용자와 매너평가점수별 평가 인원 합계를 조회 시 예외를 반환한다")
    public void getUserMannerEvaluations2() {
        //given
        given(userRepository.exists(anyLong())).willReturn(false);

        //when
        assertThatThrownBy(() -> userService.getUserMannerEvaluations(mockUser.getId()))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(userRepository).exists(anyLong());
        verify(userQueryRepository, never()).findAll(anyLong());
    }
}
