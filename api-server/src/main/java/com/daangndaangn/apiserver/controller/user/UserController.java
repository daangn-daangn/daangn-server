package com.daangndaangn.apiserver.controller.user;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.security.oauth.OAuthRequest;
import com.daangndaangn.apiserver.security.oauth.OAuthResponse;
import com.daangndaangn.apiserver.security.oauth.OAuthService;
import com.daangndaangn.apiserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.daangndaangn.apiserver.controller.ApiResult.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final OAuthService oAuthService;
    private final UserService userService;

    /**
     * POST /api/users/join
     */
    @PostMapping("/join")
    public ApiResult<UserResponse.JoinResponse> join(@Valid @RequestBody UserRequest.JoinRequest request) {
        String accessToken = request.getAccessToken();
        OAuthResponse.LoginResponse userInfo = oAuthService.getUserInfo(OAuthRequest.LoginRequest.from(accessToken));

        UserResponse.JoinResponse joinResponse = userService.join(userInfo.getId(), userInfo.getProfileImage());

        return OK(joinResponse);
    }

    /**
     * PUT /api/users
     */
    @PutMapping
    public ApiResult<Void> update(@AuthenticationPrincipal JwtAuthentication authentication,
                                  @Valid @RequestBody UserRequest.UpdateRequest request) {

        userService.update(authentication.getOauthId(),
                            request.getNickname(),
                            Location.from(request.getLocation()),
                            request.getProfileUrl());

        return OK(null);
    }
}
