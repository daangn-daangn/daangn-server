package com.daangndaangn.apiserver.security.jwt;

import com.daangndaangn.apiserver.entity.user.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Jwt jwt;

    @BeforeAll
    void setUp() {
        String issuer = "daangn-server";
        String clientSecret = "Rel5Bjce6MajBo08qgkNgYaTuzvJe6iwnBFhsD11";
        int expirySeconds = 10;
        jwt = new Jwt(issuer, clientSecret, expirySeconds);
    }

    @Test
    void JWT_토큰을_생성하고_복호화_할수있다() {
        Jwt.Claims claims = Jwt.Claims.of(1L,
                                        12312312L,
                                        "tester",
                                        Location.from("Seoul"),
                                        0.0,
                                        new String[]{"ROLE_USER"});

        String encodedJWT = jwt.createNewToken(claims);
        log.info("encodedJWT: {}", encodedJWT);

        Jwt.Claims decodedJWT = jwt.verify(encodedJWT);
        log.info("decodedJWT: {}", decodedJWT);

        assertThat(claims.id).isEqualTo(decodedJWT.id);
        assertThat(claims.oauthId).isEqualTo(decodedJWT.oauthId);
        assertThat(claims.nickname).isEqualTo(decodedJWT.nickname);
        assertThat(claims.location).isEqualTo(decodedJWT.location);
        assertThat(claims.manner).isEqualTo(decodedJWT.manner);
        assertArrayEquals(claims.roles, decodedJWT.roles);
    }

    @Test
    void JWT_토큰을_리프레시_할수있다() throws Exception {
        if (jwt.getExpirySeconds() > 0) {
            Jwt.Claims claims = Jwt.Claims.of(1L,
                    12312312L,
                    "tester",
                    Location.from("Seoul"),
                    0.0,
                    new String[]{"ROLE_USER"});

            String encodedJWT = jwt.createNewToken(claims);
            log.info("encodedJWT: {}", encodedJWT);

            // 1초 대기 후 토큰 갱신
            sleep(1_000L);

            String encodedRefreshedJWT = jwt.createRefreshToken(encodedJWT);
            log.info("encodedRefreshedJWT: {}", encodedRefreshedJWT);

            assertThat(encodedJWT).isNotEqualTo(encodedRefreshedJWT);

            Jwt.Claims oldJwt = jwt.verify(encodedJWT);
            Jwt.Claims newJwt = jwt.verify(encodedRefreshedJWT);

            long oldExp = oldJwt.exp();
            long newExp = newJwt.exp();

            // 1초 후에 토큰을 갱신했으므로, 새로운 토큰의 만료시각이 1초 이후임
            assertThat(newExp >= oldExp + 1_000L).isEqualTo(true);

            log.info("oldJwt: {}", oldJwt);
            log.info("newJwt: {}", newJwt);
        }
    }
}