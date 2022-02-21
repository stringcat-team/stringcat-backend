package com.sp.api.question.service;

import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.*;
import static com.sp.exception.type.ErrorCode.*;

import com.sp.api.question.dto.QuestionReqDto;
import com.sp.api.question.dto.QuestionResDto;
import com.sp.domain.likes.LikesCacheRepository;
import com.sp.domain.question.Question;
import com.sp.domain.question.QuestionRepository;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import com.sp.exception.type.StringcatCustomException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    private final LikesCacheRepository likesCacheRepository;

    @Transactional(readOnly = true)
    public List<QuestionResDto> getQuestions(Long userId, QuestionReqDto.Search request) {
        List<Question> questions = questionRepository.findAllQuestionByPage(request.toBrowser());
        List<Long> ids = questions.stream()
            .map(Question::getId)
            .collect(Collectors.toList());

        Map<Long, Integer> likeCountMap = likesCacheRepository.findCountList(QUESTION_LIKE_COUNT, ids);
        Map<Long, Integer> disLikeCountMap = likesCacheRepository.findCountList(QUESTION_DIS_LIKE_COUNT, ids);

        Map<Long, Boolean> likePushedMap = likesCacheRepository.findPushedList(QUESTION_LIKE, ids, userId);
        Map<Long, Boolean> disLikePushedMap = likesCacheRepository.findPushedList(QUESTION_DIS_LIKE, ids, userId);

        return questions.stream()
            .map(question -> QuestionResDto.of(question,
                likeCountMap.getOrDefault(question.getId(), 0), disLikeCountMap.getOrDefault(question.getId(), 0),
                likePushedMap.getOrDefault(question.getId(), false), disLikePushedMap.getOrDefault(question.getId(), false)))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuestionResDto getQuestion(Long userId, Long questionId) {
        Question question = findQuestionById(questionId);

        int likeCount = likesCacheRepository.findCount(QUESTION_LIKE_COUNT, questionId);
        int disLikeCount = likesCacheRepository.findCount(QUESTION_DIS_LIKE_COUNT, questionId);

        boolean likePushed = likesCacheRepository.findPushed(QUESTION_LIKE, questionId, userId);
        boolean disLikePushed = likesCacheRepository.findPushed(QUESTION_DIS_LIKE, questionId, userId);

        return QuestionResDto.of(question, likeCount, disLikeCount, likePushed, disLikePushed);
    }

    @Transactional
    public QuestionResDto createQuestion(Long userId, QuestionReqDto.Create request) {
        Optional<User> user = userRepository.findById(userId);
        Question question = questionRepository.save(request.toEntity(user.orElseThrow(NullPointerException::new)));
        return QuestionResDto.of(question, 0, 0, false, false);
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
