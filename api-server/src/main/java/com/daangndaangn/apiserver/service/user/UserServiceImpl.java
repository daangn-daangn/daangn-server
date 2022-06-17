package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.user.UserRepository;
import com.daangndaangn.common.api.repository.user.query.UserQueryDto;
import com.daangndaangn.common.api.repository.user.query.UserQueryRepository;
import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

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
        checkArgument(oauthId != null, "oauthId 값은 필수입니다.");
        checkArgument(isNotEmpty(nickname), "nickname 값은 필수입니다.");
        checkArgument(location != null && isNotEmpty(location.getAddress()), "주소값은 필수입니다.");
        checkArgument(isNotEmpty(nickname), "nickname 값은 필수입니다.");
        checkArgument(isNotEmpty(profileUrl), "profileUrl 값은 필수입니다.");

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
        checkArgument(isNotEmpty(nickname), "nickname 값은 필수입니다.");
        checkArgument(isNotEmpty(location), "주소값은 필수입니다.");

        User user = getUser(id);
        user.update(nickname, Location.from(location));
    }

    @Override
    public User getUser(Long userId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, String.format("userId = %s", userId)));
    }

    @Override
    public User getUserByOauthId(Long oauthId) {
        checkArgument(oauthId != null, "oauthId 값은 필수입니다.");
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
    public boolean isValidNickname(String nickname) {
        if (isEmpty(nickname) || nickname.length() > 20) {
            return false;
        }

        return !userRepository.existsUserByNickname(nickname);
    }

    @Override
    public List<UserQueryDto> getUserMannerEvaluations(Long userId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        if (userRepository.existsById(userId)) {
            return userQueryRepository.findAll(userId);
        }

        throw new NotFoundException(User.class, String.format("userId = %s", userId));
    }
}

