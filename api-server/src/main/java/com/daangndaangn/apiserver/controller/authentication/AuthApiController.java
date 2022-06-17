package com.daangndaangn.apiserver.controller.authentication;

import com.daangndaangn.apiserver.security.oauth.OAuthRequest;
import com.daangndaangn.apiserver.security.oauth.OAuthResponse;
import com.daangndaangn.apiserver.security.oauth.OAuthService;
import com.daangndaangn.common.jwt.JwtAuthenticationToken;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.daangndaangn.common.web.ApiResult.OK;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * 로그인/로그아웃 API 처리 controller
 */
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final OAuthService oAuthService;
    private final AuthenticationManager authenticationManager;
    private final PresignerUtils presignerUtils;

    @PostMapping("/login")
    public ApiResult<AuthResponse.LoginResponse> login(@Valid @RequestBody AuthRequest.LoginRequest loginRequest) {

        String accessToken = loginRequest.getAccessToken();

        //user에 대해 AuthenticationManager 동작
        OAuthResponse.LoginResponse oauthResponse = oAuthService.getUserInfo(OAuthRequest.LoginRequest.from(accessToken));

        JwtAuthenticationToken authToken = JwtAuthenticationToken.from(oauthResponse.getId());
        Authentication authenticate = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        AuthResponse.AuthenticationResponse response = (AuthResponse.AuthenticationResponse)authenticate.getDetails();

        String profileUrl = isEmpty(response.getProfileUrl()) ?
            null : presignerUtils.getProfilePresignedGetUrl(response.getProfileUrl());

        return OK(AuthResponse.LoginResponse.from(response, profileUrl));
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout(@Valid @RequestBody AuthRequest.LogoutRequest logoutRequest) {

        String accessToken = logoutRequest.getAccessToken();

        oAuthService.logout(OAuthRequest.LogoutRequest.from(accessToken));
        SecurityContextHolder.clearContext();

        return OK(null);
    }
}
