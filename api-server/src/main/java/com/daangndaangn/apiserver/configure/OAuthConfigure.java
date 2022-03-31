package com.daangndaangn.apiserver.configure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Kakao API(OAuth) 관련 application.yml 설정
 */
@ToString
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "oauth.kakao")
public class OAuthConfigure {

    private String headerKey;   // "Authorization"

    private String headerValue; // "Bearer"

    private String url; // "https://kapi.kakao.com/v2/user/me"

}
