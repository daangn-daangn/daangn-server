package com.daangndaangn.common.api.repository.user;

public interface UserCustomRepository {
    boolean exists(Long id);
    boolean exists(String nickname);
    boolean existsByOAuth(Long oauthId);
}
