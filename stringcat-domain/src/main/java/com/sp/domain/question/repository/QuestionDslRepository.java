package com.sp.domain.question.repository;

import com.sp.domain.question.Question;
import com.sp.domain.question.QuestionBrowser;
import java.util.List;

public interface QuestionDslRepository {

    public Question findQuestionById(Long questionId);

    public Question findQuestionByIdAndUserId(Long questionId, Long userId);

    public List<Question> findAllQuestionByPage(QuestionBrowser browser);
}
