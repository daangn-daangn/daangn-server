package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.apiserver.controller.user.UserResponse;
import com.daangndaangn.apiserver.entity.user.Location;
import com.daangndaangn.apiserver.entity.user.User;
import com.daangndaangn.apiserver.error.DuplicateValueException;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse.JoinResponse join(Long oauthId, String profileUrl) {

        User user = User.builder()
                        .oauthId(oauthId)
                        .profileUrl(profileUrl)
                        .build();

        return convertToDto(userRepository.save(user));
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
    public User login(Long oauthId) {
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new NotFoundException(User.class, String.format("oauthId = %s", oauthId)));
    }

    private UserResponse.JoinResponse convertToDto(User user) {
        return UserResponse.JoinResponse.from(user);
    }
}

