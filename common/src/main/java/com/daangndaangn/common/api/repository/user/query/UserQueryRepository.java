package com.daangndaangn.common.api.repository.user.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daangndaangn.common.api.entity.manner.QManner.manner;
import static com.daangndaangn.common.api.entity.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<UserQueryDto> findAll(Long id) {
        return jpaQueryFactory.select()
                .select(
                        Projections.constructor(UserQueryDto.class,
                            user.id,
                            manner.score,
                            manner.id.count()
                        )
                ).from(user)
                .leftJoin(manner)
                .on(user.id.eq(manner.user.id))
                .where(user.id.eq(id))
                .groupBy(user.id, manner.score)
                .fetch();
    }
}
