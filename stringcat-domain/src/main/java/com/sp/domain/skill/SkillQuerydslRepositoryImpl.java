package com.sp.domain.skill;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SkillQuerydslRepositoryImpl implements SkillQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Skill findByName(String name) {
        return queryFactory.selectFrom(QSkill.skill)
                .where(QSkill.skill.name.eq(name))
                .fetchOne();
    }

}
