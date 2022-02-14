package com.sp.domain.question.repository;

import static com.sp.domain.question.QQuestion.*;
import static com.sp.domain.question.QQuestionSkill.*;
import static com.sp.domain.question.QuestionStatus.*;
import static com.sp.domain.skill.QSkill.*;
import static com.sp.domain.user.QUser.*;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sp.domain.question.Question;
import com.sp.domain.question.QuestionBrowser;
import com.sp.domain.skill.Skill;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionDslRepositoryImpl implements QuestionDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Question findQuestionById(Long questionId) {
        return queryFactory.selectFrom(question)
            .innerJoin(question.questionSkills, questionSkill).fetchJoin()
            .innerJoin(questionSkill.skill, skill).fetchJoin()
            .innerJoin(question.user, user).fetchJoin()
            .where(
                question.id.eq(questionId),
                question.deleted.eq(ACTIVE.isStatus())
            ).fetchOne();
    }

    @Override
    public Question findQuestionByIdAndUserId(Long questionId, Long userId) {
        return queryFactory.selectFrom(question)
            .innerJoin(question.questionSkills, questionSkill).fetchJoin()
            .innerJoin(questionSkill.skill, skill).fetchJoin()
            .innerJoin(question.user, user).fetchJoin()
            .where(
                question.id.eq(questionId),
                question.user.id.eq(userId),
                question.deleted.eq(ACTIVE.isStatus())
            ).fetchOne();
    }

    @Override
    public List<Question> findAllQuestionByPage(QuestionBrowser browser) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(questionKeywordSearchFor(browser.getKeyword(), browser.getCondition()));
        booleanBuilder.and(question.deleted.eq(ACTIVE.isStatus()));
        if(browser.getSkills() != null) {
            question.in(JPAExpressions
                .select(questionSkill.question)
                .from(questionSkill)
                .where(questionSkill.skill.id.in(browser.getSkills()))
            );
        }

        List<Long> questionIds = queryFactory.select(question.id)
            .from(question)
            .where(booleanBuilder)
            .orderBy(questionSortBy(browser.getSort()))
            .offset(browser.getCursor())
            .limit(browser.getSize())
            .fetch();

        return queryFactory.selectFrom(question)
            .distinct()
            .innerJoin(question.questionSkills, questionSkill).fetchJoin()
            .innerJoin(questionSkill.skill, skill).fetchJoin()
            .innerJoin(question.user, user).fetchJoin()
            .where(
                question.id.in(questionIds)
            )
            .orderBy(question.id.desc())
            .fetch();
    }

    private BooleanExpression questionKeywordSearchFor(String keyword, String condition) {
        if(condition.equals("title")) return question.title.contains(keyword);
        else if(condition.equals("contents")) return question.contents.contains(keyword);
        else return question.title.contains(keyword).or(question.contents.contains(keyword));
    }

    private OrderSpecifier<?> questionSortBy(String sort) {
       if(sort.equals("hits")) return question.hits.desc();
       else return question.id.desc();

    }
}
