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
     *     "kakao_account": {
     *         "profile_image_needs_agreement": false,
     *         "profile": {
     *             "thumbnail_image_url": "http://k.kakaocdn.net/dn/64fac/btrgDyt8p/QFcrbregasbqBChU1/img_110x110.jpg",
     *             "profile_image_url": "http://k.kakaocdn.net/dn/64fac/btrgDyt8p/QFcrbregasbqBChU1/img_640x640.jpg",
     *         }
     *     }
     * }
     */
    @Getter
    @ToString   //FIXME: ToString은 결과 확인용으로 향후 제거할 예정입니다.
    public static class LoginResponse {
        private Long id;
        private String profileImage;

        @SuppressWarnings("unchecked")
        @JsonProperty("kakao_account")
        private void getProfileImage(Map<String, Object> kakaoAccountInfo) {
            Map<String,String> profile = (Map<String, String>) kakaoAccountInfo.get("profile");
            this.profileImage = profile.get("thumbnail_image_url");
        }
    }

    @Getter
    @ToString   //FIXME: ToString은 결과 확인용으로 향후 제거할 예정입니다.
    public static class LogoutResponse {
        private Long id;
    }
}
