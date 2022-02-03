package com.sp.domain.skill;

import com.querydsl.jpa.impl.JPAQueryFactory;

public interface SkillQuerydslRepository {

    public Skill findByName(String name);

}
