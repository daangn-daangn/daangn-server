package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.user.UserRepository;
import com.daangndaangn.common.api.repository.user.query.UserQueryDto;
import com.daangndaangn.common.api.repository.user.query.UserQueryRepository;
import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.util.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final UploadUtils uploadUtils;

    @Async
    @Override
    @Transactional
    public CompletableFuture<Long> create(Long oauthId) {
        checkArgument(oauthId != null, "oauthId 값은 필수입니다.");

        if (userRepository.existsByOAuth(oauthId)) {
            return completedFuture(getUserByOauthId(oauthId).getId());
        }

        User user = User.builder()
                .oauthId(oauthId)
                .build();

        User savedUser = userRepository.save(user);
        return completedFuture(savedUser.getId());
    }

    @Override
    @Transactional
    public long update(Long id, String nickname, Location location, String profileImageFullPath) {
        checkArgument(id != null, "id 값은 필수입니다.");
        checkArgument(isNotEmpty(nickname), "nickname 값은 필수입니다.");
        checkArgument(location != null && isNotEmpty(location.getAddress()), "주소값은 필수입니다.");
        checkArgument(isNotEmpty(profileImageFullPath), "profileImageFullPath 값은 필수입니다.");

        User user = getUser(id);

        String profileImageUrl = toProfileImageUrl(user.getId(), profileImageFullPath);

        if (isEmpty(profileImageUrl) || uploadUtils.isNotImageFile(profileImageUrl)) {
            throw new IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");
        }

        // 닉네임을 변경하는데, 이미 존재하는 닉네임인 경우
        if (!nickname.equals(user.getNickname()) && userRepository.exists(nickname)) {
            throw new DuplicateValueException(String.format("nickname: %s", nickname));
        }

        user.update(nickname, location, profileImageUrl);

        return user.getId();
    }

    private String toProfileImageUrl(Long userId, String fullPath) {
        String profileImageUrl = FilenameUtils.getName(fullPath);
        return userId + "/" + profileImageUrl;
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
    public List<User> getUsers(List<Long> userIds) {
        checkArgument(isNotEmpty(userIds), "userIds 값은 필수입니다.");
        return userRepository.findAll(userIds);
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

        return !userRepository.exists(nickname);
    }

    @Override
    public boolean existById(Long id) {
        checkArgument(id != null, "id 값은 필수입니다.");
        return userRepository.exists(id);
    }

    @Override
    public List<UserQueryDto> getUserMannerEvaluations(Long userId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        if (userRepository.exists(userId)) {
            return userQueryRepository.findAll(userId);
        }

        throw new NotFoundException(User.class, String.format("userId = %s", userId));
    }
}

