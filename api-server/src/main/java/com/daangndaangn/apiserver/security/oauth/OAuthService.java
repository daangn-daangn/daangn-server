package com.daangndaangn.apiserver.security.oauth;

/**
 * 클라이언트로부터 OAuth accessToken을 받아 OAuth Server로부터 사용자 정보를 조회하는 역할
 */
public interface OAuthService {

    OAuthResponse.LoginResponse getUserInfo(OAuthRequest.LoginRequest request);

    OAuthResponse.LogoutResponse logout(OAuthRequest.LogoutRequest request);
}
