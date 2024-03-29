package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.user.query.UserQueryDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<Long> create(Long oauthId);

    long update(Long id, String nickname, Location location, String profileImageFullPath);

    void update(Long id, String nickname, String location);

    User getUser(Long userId);

    List<User> getUsers(List<Long> userIds);

    User getUserByOauthId(Long oauthId);

    void delete(Long userId);

    boolean isValidNickname(String nickname);

    boolean existById(Long id);

    List<UserQueryDto> getUserMannerEvaluations(Long userId);
}
