package com.daangndaangn.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Date;

/**
 * JWT를 발행하는 클래스
 */
@Getter
public final class Jwt {

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public String createApiToken(User user, String[] roles) {
        Claims claims = Claims.of(user, roles);
        return createNewToken(claims);
    }

    public String createNewToken(Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);

        if (expirySeconds > 0) {
            builder.withExpiresAt(new Date(now.getTime() + (long)expirySeconds * 1_000L));
        }

        builder.withClaim("id", claims.id);
        builder.withClaim("oauthId", claims.oauthId);
        builder.withClaim("nickname", claims.nickname);
        builder.withClaim("location", ObjectUtils.isEmpty(claims.location) ? null : claims.location.getAddress());
        builder.withClaim("manner", claims.manner);
        builder.withClaim("profileUrl", claims.profileUrl);
        builder.withArrayClaim("roles", claims.roles);
        return builder.sign(algorithm);
    }

    public String createRefreshToken(String token) throws JWTVerificationException {
        Claims claims = verify(token);
        claims.eraseIat();
        claims.eraseExp();
        return createNewToken(claims);
    }

    public Claims verify(String token) throws JWTVerificationException  {
        return new Claims(jwtVerifier.verify(token));
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Claims {
        private Long id;
        private Long oauthId;
        private String nickname;
        private Location location;
        private Double manner;
        private String profileUrl;
        private String[] roles;
        private Date iat;
        private Date exp;

        private Claims(DecodedJWT decodedJWT) {
            Claim id = decodedJWT.getClaim("id");
            if (!id.isNull())
                this.id = id.asLong();
            Claim oauthId = decodedJWT.getClaim("oauthId");
            if (!oauthId.isNull())
                this.oauthId = oauthId.asLong();
            Claim nickname = decodedJWT.getClaim("nickname");
            if (!nickname.isNull())
                this.nickname = nickname.asString();
            Claim location = decodedJWT.getClaim("location");
            if (!location.isNull())
                this.location = Location.from(location.asString());
            Claim manner = decodedJWT.getClaim("manner");
            if (!manner.isNull())
                this.manner = manner.asDouble();
            Claim profileUrl = decodedJWT.getClaim("profileUrl");
            if (!profileUrl.isNull())
                this.profileUrl = profileUrl.asString();
            Claim roles = decodedJWT.getClaim("roles");
            if (!roles.isNull())
                this.roles = roles.asArray(String.class);
            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims of(User user, String[] roles) {
            Claims claims = new Claims();
            claims.id = user.getId();
            claims.oauthId = user.getOauthId();
            claims.nickname = user.getNickname();
            claims.location = user.getLocation();
            claims.manner = user.getManner();
            claims.roles = roles;
            return claims;
        }

        public static Claims of(Long id, Long oauthId, String nickname, Location location, Double manner, String[] roles) {
            Claims claims = new Claims();
            claims.id = id;
            claims.oauthId = oauthId;
            claims.nickname = nickname;
            claims.location = location;
            claims.manner = manner;
            claims.roles = roles;
            return claims;
        }

        public long iat() {
            return ObjectUtils.isNotEmpty(iat) ? iat.getTime() : -1;
        }

        public long exp() {
            return ObjectUtils.isNotEmpty(exp) ? exp.getTime() : -1;
        }

        public void eraseIat() {
            iat = null;
        }

        public void eraseExp() {
            exp = null;
        }
    }
}
