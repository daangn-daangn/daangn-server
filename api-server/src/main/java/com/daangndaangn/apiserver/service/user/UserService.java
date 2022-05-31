package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;

public interface UserService {

    Long create(Long oauthId, String profileUrl);

    void update(Long oauthId, String nickname, Location location, String profileUrl);

    void update(Long id, String nickname, String location);

    User getUser(Long userId);

    User getUserByOauthId(Long oauthId);

    void delete(Long userId);

    void updateManner(Long userId, int manner);

    boolean isValidNickname(String nickname);
}
