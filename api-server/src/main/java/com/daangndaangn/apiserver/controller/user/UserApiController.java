package com.daangndaangn.apiserver.controller.user;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.entity.user.Email;
import com.daangndaangn.apiserver.entity.user.Location;
import com.daangndaangn.apiserver.security.oauth.OAuthDto;
import com.daangndaangn.apiserver.security.oauth.OAuthService;
import com.daangndaangn.apiserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.daangndaangn.apiserver.controller.ApiResult.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final OAuthService oAuthService;
    private final UserService userService;

    @PostMapping("/join")
    public ApiResult<UserResponse.JoinResponse> join(@Valid @RequestBody UserRequest.JoinRequest request) {
        String accessToken = request.getAccessToken();
        OAuthDto.Response userInfo = oAuthService.getUserInfo(OAuthDto.Request.from(accessToken));

        UserResponse.JoinResponse joinResponse = userService.join(userInfo.getId(),
                                                                  Email.from(userInfo.getEmail()),
                                                                  request.getNickname(),
                                                                  Location.from(request.getLocation()),
                                                                  request.getProfileUrl());

        return OK(joinResponse);
    }
}
