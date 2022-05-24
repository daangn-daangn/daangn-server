package com.daangndaangn.apiserver.security;

import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.security.jwt.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class WithMockJwtAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JwtAuthentication principal = JwtAuthentication.builder()
                .id(annotation.id())
                .oauthId(annotation.oauthId())
                .nickname(annotation.nickname())
                .location(Location.from(annotation.location()))
                .profileUrl(annotation.profileUrl())
                .manner(annotation.manner()).build();

        JwtAuthenticationToken authentication = JwtAuthenticationToken.of(principal, createAuthorityList(annotation.role()));
        context.setAuthentication(authentication);
        return context;
    }
}
