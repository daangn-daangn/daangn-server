package com.daangndaangn.common.api.repository.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.daangndaangn.common.api.entity.notification.QNotification.notification;
import static com.daangndaangn.common.api.entity.user.QUser.user;

@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notification> findAllByUserId(Long userId, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(notification)
                    .join(notification.user, user).fetchJoin()
                .where(notification.user.id.eq(userId),
                        notification.isValid.eq(true)
                )
                .orderBy(notification.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                .fetch();
    }
}
