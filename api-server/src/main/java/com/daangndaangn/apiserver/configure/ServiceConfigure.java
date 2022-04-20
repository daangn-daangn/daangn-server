package com.daangndaangn.apiserver.configure;

import com.daangndaangn.apiserver.security.jwt.Jwt;
import com.daangndaangn.apiserver.util.MessageUtils;
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
public class ServiceConfigure {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer paginationConfigure() {
        return p -> p.setFallbackPageable(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id")));
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public Jwt jwt(JwtConfigure jwtConfigure) {
        return new Jwt(jwtConfigure.getIssuer(), jwtConfigure.getClientSecret(), jwtConfigure.getExpirySeconds());
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
        MessageUtils.setMessageSourceAccessor(messageSourceAccessor);
        return messageSourceAccessor;
    }
}
