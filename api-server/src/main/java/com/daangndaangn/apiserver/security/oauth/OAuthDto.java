package com.daangndaangn.apiserver.security.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * OAuthDto.Request: OAuthService에 사용자 정보를 요청하기 위해 필요한 parameter(ex. accessToken)
 * OAuthDto.Response: OAuthService의 사용자 정보 응답으로부터 사용하고자 하는 값
 */
public class OAuthDto {

    @Getter
    @AllArgsConstructor
    public static class Request {

        private String accessToken;

        public static OAuthDto.Request from(String accessToken) {
            return new OAuthDto.Request(accessToken);
        }
    }

    /**
     * json response
     *
     * {
     *     "id": 123,
     *     "kakao_account:": {
     *         "email": "test@gmail.com"
     *     }
     * }
     */
    @Getter
    @ToString   //FIXME: ToString은 결과 확인용으로 향후 제거할 예정입니다.
    public static class Response {

        private Long id;
        private String email;

        @JsonProperty("kakao_account")
        private void getUserEmail(Map<String, String> kakaoAccountInfo) {
            this.email = kakaoAccountInfo.get("email");
        }
    }
}
