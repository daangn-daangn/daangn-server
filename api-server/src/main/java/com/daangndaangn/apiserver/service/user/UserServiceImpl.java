package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.UserRepository;
import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long create(Long oauthId, String profileUrl) {

        User user = User.builder()
                .oauthId(oauthId)
                .profileUrl(profileUrl)
                .build();

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Override
    @Transactional
    public void update(Long oauthId, String nickname, Location location, String profileUrl) {

        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new NotFoundException(User.class, String.format("oauthId = %s", oauthId)));

        // 닉네임을 변경하는데, 이미 존재하는 닉네임인 경우
        if (!nickname.equals(user.getNickname()) && userRepository.existsUserByNickname(nickname)) {
            throw new DuplicateValueException(String.format("nickname: %s", nickname));
        }

        user.update(oauthId, nickname, location, profileUrl);
    }

    @Override
    @Transactional
    public void update(Long id, String nickname, String location) {
        checkArgument(isNotEmpty(nickname), "nickname must not be null");
        checkArgument(isNotEmpty(location), "location must not be null");

        User user = getUser(id);
        user.update(nickname, Location.from(location));
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, String.format("userId = %s", userId)));
    }

    @Override
    public User getUserByOauthId(Long oauthId) {
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new NotFoundException(User.class, String.format("oauthId = %s", oauthId)));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User deletedUser = getUser(userId);
        userRepository.delete(deletedUser);
    }

    @Override
    @Transactional
    public void updateManner(Long userId, int manner) {
        checkArgument(0 <= manner && manner <= 100, "manner는 0과 100사이의 값이어야 합니다.");

        User user = this.getUser(userId);

        if (manner >= 50) {
            user.increaseManner();
        } else {
            user.decreaseManner();
        }
    }

    @Override
    public boolean isValidNickname(String nickname) {
        if (isEmpty(nickname) || nickname.length() > 20) {
            return false;
        }

        return !userRepository.existsUserByNickname(nickname);
    }
}

