package com.daangndaangn.apiserver.config;

import com.daangndaangn.apiserver.config.support.ProductSearchArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(productSearchArgumentResolver());
    }

    @Bean
    public ProductSearchArgumentResolver productSearchArgumentResolver() {
        return new ProductSearchArgumentResolver();
    }
}
