package com.daangndaangn.apiserver.service.manner;

import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.manner.MannerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MannerServiceTest {

    private final Long FIRST_USER_OAUTH_ID = 111L;
    private final Long SECOND_USER_OAUTH_ID = 222L;

    @Autowired
    private UserService userService;

    @Autowired
    private MannerService mannerService;

    @Autowired
    private MannerRepository mannerRepository;  //for deleteAll

    @BeforeAll
    public void init() {
        userService.create(FIRST_USER_OAUTH_ID, null);
        userService.create(SECOND_USER_OAUTH_ID, null);
    }

    @Test
    public void 매너평가점수는_마이너스_5점에서_플러스_5점_사이어야_한다() {
        //given
        User firstUser = userService.getUserByOauthId(FIRST_USER_OAUTH_ID);
        User secondUser = userService.getUserByOauthId(SECOND_USER_OAUTH_ID);


        //when valid score
        mannerService.createManner(firstUser.getId(), secondUser.getId(), -5);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), -4);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), -3);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), -2);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), -1);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), 0);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), 1);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), 2);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), 3);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), 4);
        mannerService.createManner(firstUser.getId(), secondUser.getId(), 5);

        //when inValid score
        assertThrows(IllegalArgumentException.class, () -> mannerService.createManner(firstUser.getId(),
                secondUser.getId(), -10));
        assertThrows(IllegalArgumentException.class, () -> mannerService.createManner(firstUser.getId(),
                secondUser.getId(), -6));
        assertThrows(IllegalArgumentException.class, () -> mannerService.createManner(firstUser.getId(),
                secondUser.getId(), 6));
        assertThrows(IllegalArgumentException.class, () -> mannerService.createManner(firstUser.getId(),
                secondUser.getId(), 10));
    }

    @Test
    public void 매너평가를_0점_이상으로_받으면_매너점수가_상승한다() {
        //given
        User firstUser = userService.getUserByOauthId(FIRST_USER_OAUTH_ID);
        User secondUser = userService.getUserByOauthId(SECOND_USER_OAUTH_ID);
        double firstUserManner = firstUser.getManner();

        //when
        mannerService.createManner(firstUser.getId(), secondUser.getId(), 0);

        //then
        assertThat(firstUser.getManner()).isEqualTo(firstUserManner + 0.1);
    }

    @Test
    public void 매너평가를_0점_미만으로_받으면_매너점수가_하락한다() {
        //given
        User firstUser = userService.getUserByOauthId(FIRST_USER_OAUTH_ID);
        User secondUser = userService.getUserByOauthId(SECOND_USER_OAUTH_ID);
        double firstUserManner = firstUser.getManner();

        //when
        mannerService.createManner(firstUser.getId(), secondUser.getId(), -1);

        //then
        assertThat(firstUser.getManner()).isEqualTo(firstUserManner - 0.1);
    }

    @Test
    public void 자기_자신에_대한_매너평가는_할_수_없다() {
        //given
        User firstUser = userService.getUserByOauthId(FIRST_USER_OAUTH_ID);
        int score = 5;

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> mannerService.createManner(firstUser.getId(), firstUser.getId(), score));
    }

    @AfterAll
    public void destroy() {
        User firstUser = userService.getUserByOauthId(FIRST_USER_OAUTH_ID);
        User secondUser = userService.getUserByOauthId(SECOND_USER_OAUTH_ID);
        userService.delete(firstUser.getId());
        userService.delete(secondUser.getId());

        mannerRepository.deleteAll();
    }
}