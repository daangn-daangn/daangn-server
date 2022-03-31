package com.daangndaangn.apiserver.security.jwt;

import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * SpringSecurity 인증정보(Authentication 구현) 클래스
 */
@ToString
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 인증 주체를 나타낸다. 타입이 Object 라는 것이 중요.
     * Object인 이유는 로그인 전과 로그인 후의 타입이 다르기 때문이다.
     * 로그인 전에는 String 타입(accessToken)이고, 로그인 후에는 {@link JwtAuthentication} 타입.
     */
    private final Object principal;

    public static JwtAuthenticationToken from(Long principal) {
        return new JwtAuthenticationToken(principal);
    }

    public static JwtAuthenticationToken of(Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, authorities);
    }

    public Long getAuthenticationId() {
        return (Long)principal;
    }

    private JwtAuthenticationToken(Long principal) {
        super(null);
        super.setAuthenticated(false);

        this.principal = principal;
    }

    private JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    // Not Used
    @Override
    public Object getCredentials() {
        return null;
    }

}
