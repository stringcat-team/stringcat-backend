package com.sp.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
