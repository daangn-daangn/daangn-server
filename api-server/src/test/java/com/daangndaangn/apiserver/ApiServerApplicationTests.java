package com.daangndaangn.apiserver;

import com.daangndaangn.apiserver.entity.category.Category;
import com.daangndaangn.apiserver.entity.category.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
@SpringBootTest
class ApiServerApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Test
    void contextLoads() {

        Category category = Category.from("QueryDsl 테스트");
        entityManager.persist(category);

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCategory qCategory = QCategory.category;

        Category result = queryFactory.selectFrom(qCategory)
                                            .fetchOne();

        Assertions.assertThat(result).isEqualTo(category);
        Assertions.assertThat(result.getId()).isEqualTo(category.getId());
    }
}
