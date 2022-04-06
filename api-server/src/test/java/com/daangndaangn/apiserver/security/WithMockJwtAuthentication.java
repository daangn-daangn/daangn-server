package com.daangndaangn.apiserver.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {

    long id() default 1L;

    long oauthId() default 12315L;

    String nickname() default "tester00";

    String location() default "서울시 마포구";

    String profileUrl() default "http:localhost:8080/profile/url";

    double manner() default 36.5;

    String role() default "ROLE_USER";
}
