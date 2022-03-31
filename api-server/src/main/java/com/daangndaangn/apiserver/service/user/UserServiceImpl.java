package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.apiserver.controller.user.UserResponse;
import com.daangndaangn.apiserver.entity.user.Email;
import com.daangndaangn.apiserver.entity.user.Location;
import com.daangndaangn.apiserver.entity.user.User;
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
    public UserResponse.JoinResponse join(Long oauthId, Email email, String nickname, Location location, String profileUrl) {

        User user = User.builder()
                        .oauthId(oauthId)
                        .email(email)
                        .nickname(nickname)
                        .location(location)
                        .profileUrl(profileUrl)
                        .build();

        return convertToDto(userRepository.save(user));
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

