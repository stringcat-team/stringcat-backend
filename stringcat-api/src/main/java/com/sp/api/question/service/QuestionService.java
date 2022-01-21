package com.sp.api.question.service;

import static com.sp.exception.type.ErrorCode.*;

import com.sp.api.question.dto.QuestionReqDto;
import com.sp.api.question.dto.QuestionResDto;
import com.sp.domain.question.Question;
import com.sp.domain.question.QuestionRepository;
import com.sp.domain.skill.SkillRepository;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import com.sp.exception.type.StringcatCustomException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<QuestionResDto> getQuestions(QuestionReqDto.Search request) {
        List<Question> questions = questionRepository.findAllQuestionByPage(request.toBrowser());

        return questions.stream()
            .map(QuestionResDto::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuestionResDto getQuestion(Long questionId) {
        Question question = findQuestionById(questionId);
        return QuestionResDto.of(question);
    }

    @Transactional
    public void createQuestion(Long userId, QuestionReqDto.Create request) {
        Optional<User> user = userRepository.findById(userId);

        questionRepository.save(request.toEntity(user.orElseThrow(NullPointerException::new)));
    }

    @Transactional
    public void updateQuestion(Long userId, Long questionId, QuestionReqDto.Update request) {
        Question question = findQuestionByIdAndUserId(questionId, userId);
        question.update(request.getTitle(), request.getContents(), request.toSkills(question));
    }

    @Transactional
    public void deleteQuestion(Long userId, Long questionId) {
        Question question = findQuestionByIdAndUserId(questionId, userId);
        question.delete();
    }

    private Question findQuestionById(Long questionId) {
        Question question = questionRepository.findQuestionById(questionId);
        validationQuestionIsNull(question);
        return question;
    }

    private Question findQuestionByIdAndUserId(Long questionId, Long userId) {
        Question question = questionRepository.findQuestionByIdAndUserId(questionId, userId);
        validationQuestionIsNull(question);
        return question;
    }

    private void validationQuestionIsNull(Question question) {
        if(question == null) throw new StringcatCustomException("존재하지 않는 질문 입니다.", NOT_FOUNT_QUESTION);
    }
}
