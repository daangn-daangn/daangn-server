package com.daangndaangn.common.configure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 프로젝트 어디에서나 JPAQueryFactory를 주입받아 Querydsl을 사용할 수 있도록 빈 등록
 */
@Configuration
public class QuerydslConfigure {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
