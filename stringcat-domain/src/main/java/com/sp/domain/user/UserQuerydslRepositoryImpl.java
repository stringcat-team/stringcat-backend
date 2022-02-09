package com.sp.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQuerydslRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public User findBySocialId(String socialId) {
        return queryFactory
                .selectFrom(QUser.user)
                .where(QUser.user.socialId.eq(socialId))
                .fetchOne();

    }

}
