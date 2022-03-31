package com.daangndaangn.apiserver.security.oauth;

import com.daangndaangn.apiserver.configure.OAuthConfigure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 클라이언트로부터 OAuth accessToken을 받아 OAuth Server로부터 사용자 정보를 조회하는 클래스
 *
 * 구현체인 KakaoOAuthService는 카카오 API 스펙을 따른다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

    private final RestTemplate restTemplate;

    private final OAuthConfigure oAuthConfigure;

    @Override
    public OAuthDto.Response getUserInfo(OAuthDto.Request request) {

        HttpEntity authRequest = createAuthRequest(request);
        String requestUrl = oAuthConfigure.getUrl();

        ResponseEntity<OAuthDto.Response> oauthResponse
                = restTemplate.exchange(requestUrl, HttpMethod.GET, authRequest, OAuthDto.Response.class);

        log.info("response body = {}", oauthResponse.getBody());

        return oauthResponse.getBody();
    }

    /**
     * HttpHeader key: Authorization
     * HttpHeader value: Bearer ${ACCESS_TOKEN}
     *
     * @return Authorization: Bearer ${ACCESS_TOKEN}
     */
    private HttpEntity createAuthRequest(OAuthDto.Request authRequest) {
        String headerValue = createRequestHeader(authRequest.getAccessToken());

        HttpHeaders headers = new HttpHeaders();
        headers.set(oAuthConfigure.getHeaderKey(), headerValue);

        return new HttpEntity(headers);
    }

    /**
     * @return Bearer ${ACCESS_TOKEN}
     */
    private String createRequestHeader(String accessToken) {
        return String.format("%s %s", oAuthConfigure.getHeaderValue(), accessToken);
    }
}
