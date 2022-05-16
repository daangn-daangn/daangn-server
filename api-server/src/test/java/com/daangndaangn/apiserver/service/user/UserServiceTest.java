package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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

        userService.join(USER_OAUTH_ID, null);
    }

    @Test
    public void 매너평가를_오십점_이상으로_받으면_매너점수가_상승한다() {
        //given
        User beforeUser = userService.findUserByOauthId(USER_OAUTH_ID);
        double before = beforeUser.getManner();

        //when
        userService.updateManner(beforeUser.getId(), 50);
        User afterUser = userService.findUserByOauthId(USER_OAUTH_ID);

        //then
        Assertions.assertThat(afterUser.getManner()).isEqualTo(before + 0.1);
    }

    @Test
    public void 매너평가를_오십점_미만으로_받으면_매너점수가_하락한다() {
        //given
        User beforeUser = userService.findUserByOauthId(USER_OAUTH_ID);
        double before = beforeUser.getManner();

        //when
        userService.updateManner(beforeUser.getId(), 10);
        User afterUser = userService.findUserByOauthId(USER_OAUTH_ID);

        //then
        Assertions.assertThat(afterUser.getManner()).isEqualTo(before - 0.1);
    }

    @AfterAll
    public void destroy() {
        User user = userService.findUserByOauthId(USER_OAUTH_ID);
        userService.delete(user.getId());
    }
}