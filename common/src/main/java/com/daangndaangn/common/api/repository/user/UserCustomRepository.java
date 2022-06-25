package com.daangndaangn.common.api.repository.user;

import com.daangndaangn.common.api.entity.user.User;

import java.util.List;

public interface UserCustomRepository {
    boolean exists(Long id);
    boolean exists(String nickname);
    boolean existsByOAuth(Long oauthId);
    List<User> findAll(List<Long> userIds);
}
