package com.daangndaangn.common.api.repository.user;

import com.daangndaangn.common.api.entity.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.daangndaangn.common.api.entity.user.QUser.user;


@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean exists(Long id) {
        User result = jpaQueryFactory
                .selectFrom(user)
                .where(user.id.eq(id))
                .fetchFirst();

        return result != null;
    }

    @Override
    public boolean exists(String nickname) {
        User result = jpaQueryFactory
                .selectFrom(user)
                .where(user.nickname.eq(nickname))
                .fetchFirst();

        return result != null;
    }

    @Override
    public boolean existsByOAuth(Long oauthId) {
        User result = jpaQueryFactory
                .selectFrom(user)
                .where(user.oauthId.eq(oauthId))
                .fetchFirst();

        return result != null;
    }
}
