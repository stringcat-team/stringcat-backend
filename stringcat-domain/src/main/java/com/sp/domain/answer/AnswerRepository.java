package com.sp.domain.answer;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    public List<Answer> findByQuestionIdAndDeleted(Long questionId, boolean deleted);
    public boolean existsByQuestionIdAndUserId(Long questionId, Long userId);
}
