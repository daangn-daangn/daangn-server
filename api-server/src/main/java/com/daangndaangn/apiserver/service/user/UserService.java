package com.daangndaangn.apiserver.service.user;

import com.daangndaangn.apiserver.controller.user.UserResponse;
import com.daangndaangn.apiserver.entity.user.Location;
import com.daangndaangn.apiserver.entity.user.User;

public interface UserService {

    UserResponse.JoinResponse join(Long oauthId, String profileUrl);

    void update(Long oauthId, String nickname, Location location, String profileUrl);

    User login(Long oauthId);
}
