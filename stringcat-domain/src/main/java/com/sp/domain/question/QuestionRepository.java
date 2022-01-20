package com.sp.domain.question;

import com.sp.domain.question.repository.QuestionDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionDslRepository {

}
