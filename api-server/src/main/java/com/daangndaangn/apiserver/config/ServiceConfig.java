package com.daangndaangn.apiserver.config;

import com.daangndaangn.common.config.JwtConfig;
import com.daangndaangn.common.jwt.Jwt;
import com.daangndaangn.common.util.MessageUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.client.RestTemplate;

/**
 * 프로젝트에서 사용하는 각종 Bean 모음
 */
@Configuration
public class ServiceConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer paginationConfigure() {
        return p -> p.setFallbackPageable(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id")));
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public Jwt jwt(JwtConfig jwtConfig) {
        return new Jwt(jwtConfig.getIssuer(), jwtConfig.getClientSecret(), jwtConfig.getExpirySeconds());
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
        MessageUtils.setMessageSourceAccessor(messageSourceAccessor);
        return messageSourceAccessor;
    }
}
