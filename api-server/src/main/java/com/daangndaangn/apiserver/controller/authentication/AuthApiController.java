package com.daangndaangn.apiserver.controller.authentication;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.security.jwt.JwtAuthenticationToken;
import com.daangndaangn.apiserver.security.oauth.OAuthDto;
import com.daangndaangn.apiserver.security.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 API 처리 controller
 */
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final OAuthService oAuthService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ApiResult<AuthenticationDto.Response> authentication(@RequestBody AuthenticationDto.Request authRequest) {

        //accessToken to User
        String accessToken = authRequest.getAccessToken();

        //user에 대해 AuthenticationManager 동작
        OAuthDto.Response oauthResponse = oAuthService.getUserInfo(OAuthDto.Request.from(accessToken));

        JwtAuthenticationToken authToken = JwtAuthenticationToken.from(oauthResponse.getId());
        Authentication authenticate = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        AuthenticationDto.Response response = (AuthenticationDto.Response)authenticate.getDetails();

        return ApiResult.OK(response);
    }
}
