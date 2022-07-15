package com.daangndaangn.apiserver.service.manner;

import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.manner.Manner;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.manner.MannerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MannerServiceTest {

    @InjectMocks
    private MannerServiceImpl mannerService;

    @Mock
    private UserService userService;

    @Mock
    private MannerRepository mannerRepository;

    private User mockUser, mockEvaluator;

    @BeforeEach
    public void init() {
        mockUser = User.builder()
                .id(1L)
                .oauthId(12345L)
                .build();

        mockUser.update("testNickname", Location.from("test Address"));

        mockEvaluator = User.builder()
                .id(2L)
                .oauthId(56789L)
                .build();

        mockEvaluator.update("testNickname", Location.from("test Address"));
    }

    @Test
    @DisplayName("매너평가를_0점_이상으로_받으면_매너점수가_상승한다")
    public void createManner1() {
        //given
        int score = 0;

        Manner mockManner = Manner.builder()
                .id(1L)
                .user(mockUser)
                .evaluator(mockEvaluator)
                .score(score)
                .build();

        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(mannerRepository.save(any())).willReturn(mockManner);

        double before = mockUser.getManner();

        //when
        mannerService.createManner(mockUser.getId(), mockEvaluator.getId(), score);

        //then
        double after = mockUser.getManner();

        assertThat(before).isLessThan(after);
        verify(userService, times(2)).getUser(anyLong());
        verify(mannerRepository).save(any());
    }

    @Test
    @DisplayName("매너평가점수는_마이너스_5점에서_플러스_5점_사이여야_한다")
    public void createManner2() {
        //given
        List<Integer> invalidScores = List.of(-10, -9, -8, -7, -6, 6, 7, 8, 9, 10);

        //when
        for (int invalidScore : invalidScores) {
            assertThrows(IllegalArgumentException.class,
                    () -> mannerService.createManner(mockUser.getId(), mockEvaluator.getId(), invalidScore));
        }

        //then
        verify(userService, never()).getUser(anyLong());
        verify(mannerRepository, never()).save(any());
    }

    @Test
    @DisplayName("매너평가를_0점_미만으로_받으면_매너점수가_하락한다")
    public void createManner3() {
        //given
        int score = -1;

        Manner mockManner = Manner.builder()
                .id(1L)
                .user(mockUser)
                .evaluator(mockEvaluator)
                .score(score)
                .build();

        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(mannerRepository.save(any())).willReturn(mockManner);

        double before = mockUser.getManner();

        //when
        mannerService.createManner(mockUser.getId(), mockEvaluator.getId(), score);

        //then
        double after = mockUser.getManner();

        assertThat(before).isGreaterThan(after);
        verify(userService, times(2)).getUser(anyLong());
        verify(mannerRepository).save(any());
    }

    @Test
    @DisplayName("자기_자신에_대한_매너평가는_할_수_없다")
    public void createManner4() {
        //given
        long invalidUserId = 1L;
        long invalidEvaluatorId = 1L;
        int score = 5;

        //when
        assertThrows(IllegalArgumentException.class,
            () -> mannerService.createManner(invalidUserId, invalidEvaluatorId, score));

        //then
        verify(userService, never()).getUser(anyLong());
        verify(mannerRepository, never()).save(any());
    }
}
