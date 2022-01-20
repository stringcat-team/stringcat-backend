package com.sp.domain.question;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "select distinct q from Question q "
                 + "join fetch q.questionSkills "
                 + "join fetch q.user "
                 + "where q.id = :questionId")
    public Optional<Question> findById(Long questionId);

    public Optional<Question> findByIdAndUserId(Long questionId, Long userId);
}
