package com.daangndaangn.apiserver.configure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 프로젝트 어디에서나 JPAQueryFactory를 주입받아 Querydsl을 사용할 수 있도록 빈 등록
 *
 * 현재 rdb(Mysql) 뿐만 아니라 채팅방 정보 조회를 위해 api-server에서도 nosql(mongodb)를 사용할 것 같아서
 * multi-datasource 적용을 위해 미리 rdb prefix 사용 (향후에 수정 필요)
 */
@Configuration
public class QuerydslConfigure {

//    @PersistenceContext(unitName = "rdbEntityManager")
    @PersistenceContext
    private EntityManager rdbEntityManager;

    @Bean
    @Primary
    public JPAQueryFactory rdbJpaQueryFactory() {
        return new JPAQueryFactory(rdbEntityManager);
    }
}
