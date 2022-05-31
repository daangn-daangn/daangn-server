package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {

    private final Long USER_OAUTH_ID = 111L;

    @Autowired
    private UserService userService;

    @BeforeAll
    public void init() {
        userService.create(USER_OAUTH_ID, null);
    }

    @Test
    public void 유저는_닉네임과_사는_동네를_업데이트_할_수_있다() {
        //given
        User user = userService.getUserByOauthId(USER_OAUTH_ID);

        assertThat(user.getNickname()).isNull();
        assertThat(user.getLocation()).isNull();

        String nickname = "테스트 유저";
        String address = "노원구 상계동";

        //when
        userService.update(user.getId(), nickname, address);

        //then
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getLocation().getAddress()).isEqualTo(address);
    }

    @Test
    public void 매너평가를_오십점_이상으로_받으면_매너점수가_상승한다() {
        //given
        User beforeUser = userService.getUserByOauthId(USER_OAUTH_ID);
        double before = beforeUser.getManner();

        //when
        userService.updateManner(beforeUser.getId(), 50);
        User afterUser = userService.getUserByOauthId(USER_OAUTH_ID);

        //then
        assertThat(afterUser.getManner()).isEqualTo(before + 0.1);
    }

    @Test
    public void 매너평가를_오십점_미만으로_받으면_매너점수가_하락한다() {
        //given
        User beforeUser = userService.getUserByOauthId(USER_OAUTH_ID);
        double before = beforeUser.getManner();

        //when
        userService.updateManner(beforeUser.getId(), 10);
        User afterUser = userService.getUserByOauthId(USER_OAUTH_ID);

        //then
        assertThat(afterUser.getManner()).isEqualTo(before - 0.1);
    }

    @Test
    public void 닉네임_중복여부를_확인할_수_있다() {
        //given
        User user = userService.getUserByOauthId(USER_OAUTH_ID);

        String nickname = "테스트 유저";
        String address = "노원구 상계동";

        userService.update(user.getId(), nickname, address);

        //when
        String duplicatedNickname = nickname;
        String validNickname = "테스트 유저2";

        //then
        assertThat(userService.isValidNickname(duplicatedNickname)).isEqualTo(false);
        assertThat(userService.isValidNickname(validNickname)).isEqualTo(true);
    }

    @AfterAll
    public void destroy() {
        User user = userService.getUserByOauthId(USER_OAUTH_ID);
        userService.delete(user.getId());
    }
}