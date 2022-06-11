package com.daangndaangn.apiserver.controller.user;

import com.daangndaangn.apiserver.controller.user.UserResponse.JoinResponse;
import com.daangndaangn.apiserver.controller.user.UserResponse.MannerResponse;
import com.daangndaangn.apiserver.controller.user.UserResponse.NicknameResponse;
import com.daangndaangn.apiserver.controller.user.UserResponse.UserInfoResponse;
import com.daangndaangn.apiserver.service.manner.MannerService;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.security.oauth.OAuthRequest;
import com.daangndaangn.apiserver.security.oauth.OAuthResponse;
import com.daangndaangn.apiserver.security.oauth.OAuthService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final OAuthService oAuthService;
    private final UserService userService;
    private final MannerService mannerService;

    /**
     * GET /api/users/:userId
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

        mannerService.createManner(request.getUserId(), authentication.getId(), request.getScore());

        return OK(null);
    }

    /**
     * GET /api/users/manner/:userId
     */
    @GetMapping("/manner/{userId}")
    public ApiResult<List<MannerResponse>> getUserManner(@PathVariable("userId") Long userId) {
        List<MannerResponse> mannerResponses = userService.getUserMannerEvaluations(userId).stream()
                .map(userQueryDto -> MannerResponse.of(userQueryDto.getScore(), userQueryDto.getScoreCount()))
                .collect(toList());

        return OK(mannerResponses);
    }

    /**
     * GET /api/users/nickname
     */
    @GetMapping("/nickname")
    public ApiResult<NicknameResponse> isValidNickname(@RequestParam("name") String name) {
        return OK(NicknameResponse.from(userService.isValidNickname(name)));
    }
}
