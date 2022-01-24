package com.sp.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public User findBySocialId(String socialId) {
        return queryFactory
                .selectFrom(QUser.user)
                .where(QUser.user.socialId.eq(socialId))
                .fetchOne();
    }


}
