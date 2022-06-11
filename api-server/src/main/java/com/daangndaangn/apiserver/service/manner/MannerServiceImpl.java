package com.daangndaangn.apiserver.service.manner;

import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.manner.Manner;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.manner.MannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkArgument;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MannerServiceImpl implements MannerService {

    private final MannerRepository mannerRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Long createManner(Long userId, Long evaluatorId, int score) {
        checkArgument(userId != null, "userId must not be null");
        checkArgument(evaluatorId != null, "evaluatorId must not be null");
        checkArgument(-5 <= score && score <= 5, "score");
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

        return manner.getId();
    }
}
