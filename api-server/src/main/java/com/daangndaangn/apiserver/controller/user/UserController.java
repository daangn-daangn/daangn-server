package com.daangndaangn.apiserver.controller.user;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.controller.user.UserResponse.JoinResponse;
import com.daangndaangn.apiserver.controller.user.UserResponse.NicknameResponse;
import com.daangndaangn.apiserver.controller.user.UserResponse.UserInfoResponse;
import com.daangndaangn.apiserver.error.UnauthorizedException;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.security.oauth.OAuthRequest;
import com.daangndaangn.apiserver.security.oauth.OAuthResponse;
import com.daangndaangn.apiserver.security.oauth.OAuthService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.user.User;
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
     * GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    public ApiResult<UserInfoResponse> getUser(@PathVariable("userId") Long userId) {
        return OK(UserInfoResponse.from(userService.getUser(userId)));
    }

    /**
     * POST /api/users/join
     */
    @PostMapping("/join")
    public ApiResult<JoinResponse> join(@Valid @RequestBody UserRequest.JoinRequest request) {
        String accessToken = request.getAccessToken();
        OAuthResponse.LoginResponse userInfo = oAuthService.getUserInfo(OAuthRequest.LoginRequest.from(accessToken));

        Long userId = userService.create(userInfo.getId(), userInfo.getProfileImage());
        User user = userService.getUser(userId);
        return OK(JoinResponse.from(user));
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

    /**
     * POST /api/users/manner
     */
    @PostMapping("/manner")
    public ApiResult<Void> updateManner(@AuthenticationPrincipal JwtAuthentication authentication,
                                        @Valid @RequestBody UserRequest.MannerRequest request) {

        if (authentication.getId().equals(request.getUserId())) {
            throw new UnauthorizedException("자기 자신은 매너평가를 할 수 없습니다.");
        }

        userService.updateManner(request.getUserId(), request.getScore());

        return OK(null);
    }

    /**
     * GET /api/users/nickname
     */
    @GetMapping("/nickname")
    public ApiResult<NicknameResponse> isValidNickname(@RequestParam("name") String name) {
        return OK(NicknameResponse.from(userService.isValidNickname(name)));
    }
}
