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
import com.daangndaangn.common.controller.ApiResult;
import com.daangndaangn.common.controller.ErrorResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.daangndaangn.common.controller.ApiResult.OK;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final OAuthService oAuthService;
    private final UserService userService;
    private final MannerService mannerService;
    private final PresignerUtils presignerUtils;

    /**
     * GET /api/users
     */
    @GetMapping
    public ApiResult<UserInfoResponse> getUser(@AuthenticationPrincipal JwtAuthentication authentication) {

        User user = userService.getUser(authentication.getId());
        String profileUrl = isEmpty(user.getProfileUrl()) ?
                null : presignerUtils.getProfilePresignedGetUrl(user.getProfileUrl());

        return OK(UserInfoResponse.from(user, profileUrl));
    }

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
     *
     * success: JoinResponse
     */
    @PostMapping("/join")
    public CompletableFuture<ResponseEntity<ApiResult<?>>> join(@Valid @RequestBody UserRequest.JoinRequest request) {
        String accessToken = request.getAccessToken();
        OAuthResponse.LoginResponse userInfo = oAuthService.getUserInfo(OAuthRequest.LoginRequest.from(accessToken));

        return userService.create(userInfo.getId()).handle((userId, throwable) -> {
            if (userId != null) {
                User user = userService.getUser(userId);
                return new ResponseEntity<>(OK(JoinResponse.of(user, userInfo.getProfileImage())), OK);
            }

            return ErrorResponseEntity.from(throwable, true);
        });
    }

    /**
     * PUT /api/users
     */
    @PutMapping
    public ApiResult<UpdateResponse> update(@AuthenticationPrincipal JwtAuthentication authentication,
                                            @Valid @RequestBody UserRequest.UpdateRequest request) {

        long userId = userService.update(authentication.getId(),
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
     *
     * success: Void
     */
    @PostMapping("/manner")
    public CompletableFuture<ResponseEntity<ApiResult<?>>> updateManner(
                                        @AuthenticationPrincipal JwtAuthentication authentication,
                                        @Valid @RequestBody UserRequest.MannerRequest request) {

        if (authentication.getId().equals(request.getUserId())) {
            throw new UnauthorizedException("자기 자신은 매너평가를 할 수 없습니다.");
        }

        Long userId = request.getUserId();
        Long evaluatorId = authentication.getId();
        int score = request.getScore();

        return mannerService.createManner(userId, evaluatorId, score).handle((mannerId, throwable) -> {
            if (mannerId != null) {
                return new ResponseEntity<>(OK(), OK);
            }

            return ErrorResponseEntity.from(throwable, true);
        });
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
