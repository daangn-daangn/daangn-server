package com.daangndaangn.apiserver.security;

import com.daangndaangn.apiserver.controller.authentication.AuthResponse;
import com.daangndaangn.common.api.entity.user.Role;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.jwt.Jwt;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.jwt.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.ClassUtils.isAssignable;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

/**
 * 사용자 요청으로부터 들어온 인증주체를 검증하는 클래스 (실제 인증 로직 처리 역할)
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;

    private final UserService userService;

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return createUserAuthentication(authenticationToken.getAuthenticationId());
    }

    private Authentication createUserAuthentication(Long authenticationId) {
        try {
            User user = userService.getUserByOauthId(authenticationId);
            JwtAuthentication jwtAuthentication = JwtAuthentication.from(user);
            JwtAuthenticationToken authenticationToken =
                    JwtAuthenticationToken.of(jwtAuthentication, createAuthorityList(Role.USER.value()));

            String apiToken = jwt.createApiToken(user, new String[]{Role.USER.value()});
            authenticationToken.setDetails(AuthResponse.AuthenticationResponse.of(apiToken, user));

            return authenticationToken;
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
