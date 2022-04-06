package com.daangndaangn.apiserver.security.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

public class OAuthResponse {

    /**
     * OAuthService의 사용자 정보 응답으로부터 사용하고자 하는 값
     *
     * json response(e.g)
     *
     * {
     *     "id": 123,
     *     "kakao_account:": {
     *         "email": "test@gmail.com",,
     *         "profile_image": ""
     *     }
     * }
     */
    @Getter
    @ToString   //FIXME: ToString은 결과 확인용으로 향후 제거할 예정입니다.
    public static class LoginResponse {
        private Long id;
        private String profileImage;

        @JsonProperty("kakao_account")
        private void getProfileImage(Map<String, String> kakaoAccountInfo) {
            this.profileImage = kakaoAccountInfo.get("profile_image");
        }
    }

    @Getter
    @ToString   //FIXME: ToString은 결과 확인용으로 향후 제거할 예정입니다.
    public static class LogoutResponse {
        private Long id;
    }
}
