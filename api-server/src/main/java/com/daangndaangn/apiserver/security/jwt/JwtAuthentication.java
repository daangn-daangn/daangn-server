package com.daangndaangn.apiserver.security.jwt;

import com.daangndaangn.apiserver.entity.user.Email;
import com.daangndaangn.apiserver.entity.user.Location;
import com.daangndaangn.apiserver.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 인증된 사용자를 표현
 */
@Builder
@Getter
@RequiredArgsConstructor
public class JwtAuthentication {

    private final Long id;

    private final Long oauthId;

    private final Email email;

    private final String nickname;

    private final Location location;

    private final String profileUrl;

    private final Double manner;

    public static JwtAuthentication from(User user) {
        return JwtAuthentication.builder()
                .id(user.getId())
                .oauthId(user.getOauthId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .location(user.getLocation())
                .profileUrl(user.getProfileUrl())
                .manner(user.getManner())
                .build();
    }
}
