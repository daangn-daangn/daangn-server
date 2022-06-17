package com.daangndaangn.apiserver.controller.user;

import com.daangndaangn.apiserver.controller.user.UserResponse.*;
import com.daangndaangn.apiserver.service.manner.MannerService;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.apiserver.security.oauth.OAuthRequest;
import com.daangndaangn.apiserver.security.oauth.OAuthResponse;
import com.daangndaangn.apiserver.security.oauth.OAuthService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final OAuthService oAuthService;
    private final UserService userService;
    private final MannerService mannerService;
    private final PresignerUtils presignerUtils;

    /**
     * GET /api/users/:userId
     */
    @GetMapping("/{userId}")
    public ApiResult<UserInfoResponse> getUser(@PathVariable("userId") Long userId) {

        User user = userService.getUser(userId);
        String profileUrl = isEmpty(user.getProfileUrl()) ?
            null : presignerUtils.getProfilePresignedGetUrl(user.getProfileUrl());

        return OK(UserInfoResponse.from(user, profileUrl));
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
    public ApiResult<UpdateResponse> update(@AuthenticationPrincipal JwtAuthentication authentication,
                                            @Valid @RequestBody UserRequest.UpdateRequest request) {

        long userId = userService.update(authentication.getOauthId(),
                request.getNickname(),
                Location.from(request.getLocation()),
                request.getProfileUrl());

        User user = userService.getUser(userId);
        String profileUrl = isEmpty(user.getProfileUrl()) ?
                null : presignerUtils.getProfilePresignedPutUrl(user.getProfileUrl());

        return OK(UpdateResponse.of(user, profileUrl));
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
