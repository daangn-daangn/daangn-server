package com.daangndaangn.apiserver.service.manner;

import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.manner.Manner;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.manner.MannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MannerServiceImpl implements MannerService {

    private final MannerRepository mannerRepository;
    private final UserService userService;

    @Async
    @Override
    @Transactional
    public CompletableFuture<Long> createManner(Long userId, Long evaluatorId, int score) {
        checkArgument(userId != null, "userId 값은 필수입니다.");
        checkArgument(evaluatorId != null, "evaluatorId 값은 필수입니다.");
        checkArgument(-5 <= score && score <= 5, "score는 -5점과 5점 사이여야 합니다.");
        checkArgument(!userId.equals(evaluatorId), "userId와 evaluatorId는 달라야 합니다.");

        User user = userService.getUser(userId);
        User evaluator = userService.getUser(evaluatorId);

        Manner manner = Manner.builder()
                .user(user)
                .evaluator(evaluator)
                .score(score)
                .build();

        mannerRepository.save(manner);

        if (score >= 0) {
            user.increaseManner();
        } else {
            user.decreaseManner();
        }

        return completedFuture(manner.getId());
    }
}
