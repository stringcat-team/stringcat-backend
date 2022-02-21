package com.sp.api.answer.service;

import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.ANSWER_DIS_LIKE;
import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.ANSWER_DIS_LIKE_COUNT;
import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.ANSWER_LIKE;
import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.ANSWER_LIKE_COUNT;
import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.QUESTION_DIS_LIKE;
import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.QUESTION_DIS_LIKE_COUNT;
import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.QUESTION_LIKE;
import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.QUESTION_LIKE_COUNT;
import static com.sp.exception.type.ErrorCode.*;

import com.sp.api.answer.dto.AnswerReqDto;
import com.sp.api.answer.dto.AnswerResDto;
import com.sp.api.question.dto.QuestionResDto;
import com.sp.domain.answer.Answer;
import com.sp.domain.answer.AnswerRepository;
import com.sp.domain.likes.LikesCacheRepository;
import com.sp.domain.question.Question;
import com.sp.domain.question.QuestionRepository;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final LikesCacheRepository likesCacheRepository;

    @Transactional(readOnly = true)
    public List<AnswerResDto> getAnswers(Long questionId, Long userId) {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeleted(questionId, false);
        List<Long> ids = answers.stream()
            .map(Answer::getId)
            .collect(Collectors.toList());

        Map<Long, Integer> likeCountMap = likesCacheRepository.findCountList(ANSWER_LIKE_COUNT, ids);
        Map<Long, Integer> disLikeCountMap = likesCacheRepository.findCountList(ANSWER_DIS_LIKE_COUNT, ids);

        Map<Long, Boolean> likePushedMap = likesCacheRepository.findPushedList(ANSWER_LIKE, ids, userId);
        Map<Long, Boolean> disLikePushedMap = likesCacheRepository.findPushedList(ANSWER_DIS_LIKE, ids, userId);

        return answers.stream()
            .map(answer -> AnswerResDto.of(answer,
                likeCountMap.getOrDefault(answer.getId(), 0), disLikeCountMap.getOrDefault(answer.getId(), 0),
                likePushedMap.getOrDefault(answer.getId(), false), disLikePushedMap.getOrDefault(answer.getId(), false)))
            .collect(Collectors.toList());
    }

    @Transactional
    public AnswerResDto createAnswer(Long questionId, Long userId, AnswerReqDto.Create request) {
        if(answerRepository.existsByQuestionIdAndUserId(questionId, userId)) {
            throw new StringcatCustomException("이미 답변이 등록되어있습니다.", EXISTS_ANSWER);
        }

        Question question = questionRepository.findById(questionId).orElseThrow(NullPointerException::new);
        User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);

        if(user.getId().equals(userId)) {
            throw new StringcatCustomException("자신이 작성한 질문에 답변할 수 없습니다.", SELF_WRITE);
        }

        Answer answer = answerRepository.save(request.toEntity(question, user));
        return AnswerResDto.of(answer, 0, 0, false, false);
    }

    @Transactional
    public void updateAnswer(Long answerId, Long userId, AnswerReqDto.Update request) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(NullPointerException::new);
        validationWriter(answer.getUser().getId(), userId);
        answer.updateInfo(request.getContents());
    }

    @Transactional
    public void deleteAnswer(Long answerId, Long userId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(NullPointerException::new);
        validationWriter(answer.getUser().getId(), userId);
        answer.delete();
    }

    private void validationWriter(Long writerId, Long loginId) {
        if(!writerId.equals(loginId)) {
            throw new StringcatCustomException("작성자가 아닙니다.", MISMATCH_WRITER);
        }
    }
}
