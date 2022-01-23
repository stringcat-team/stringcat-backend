package com.sp.api.likes.service;

import static com.sp.common.utils.RedisKeyUtils.KeyPrefix.*;

import com.sp.api.likes.dto.LikesPushRes;
import com.sp.common.utils.RedisKeyUtils;
import com.sp.common.utils.RedisKeyUtils.KeyPrefix;
import com.sp.domain.likes.LikesCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesCommandService likesCommandService;
    private final LikesCacheRepository likesCacheRepository;

    public LikesPushRes questionLikePush(Long userId, Long questionId) {
    return push(userId, questionId, QUESTION_LIKE, QUESTION_LIKE_COUNT);
    }

    public LikesPushRes questionDisLikePush(Long userId, Long questionId) {
        return push(userId, questionId, QUESTION_DIS_LIKE, QUESTION_DIS_LIKE_COUNT);
    }

    public LikesPushRes answerLikePush(Long userId, Long questionId) {
        return push(userId, questionId, ANSWER_LIKE, ANSWER_LIKE_COUNT);
    }

    public LikesPushRes answerDisLikePush(Long userId, Long questionId) {
        return push(userId, questionId, ANSWER_DIS_LIKE, ANSWER_DIS_LIKE_COUNT);
    }

    private LikesPushRes push(Long userId, Long questionId, KeyPrefix generalKeyPrefix, KeyPrefix countKeyPrefix) {
        if(likesCacheRepository.exists(generalKeyPrefix, userId, questionId)) {
            likesCommandService.removeAndDecrease(userId, questionId, generalKeyPrefix, countKeyPrefix);
            return LikesPushRes.of(false);
        } else {
            likesCommandService.addAndIncrease(userId, questionId, generalKeyPrefix, countKeyPrefix);
            return LikesPushRes.of(true);
        }
    }
}
