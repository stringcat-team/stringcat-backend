package com.sp.api.likes.service;

import com.sp.common.utils.RedisKeyUtils.KeyPrefix;
import com.sp.domain.likes.LikesCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesCommandService {

    private final LikesCacheRepository likesCacheRepository;

    @Transactional
    public void removeAndDecrease(Long userId, Long questionId, KeyPrefix generalKeyPrefix, KeyPrefix countKeyPrefix) {
        likesCacheRepository.remove(generalKeyPrefix, userId, questionId);
        likesCacheRepository.decrease(countKeyPrefix, questionId);
    }

    @Transactional
    public void addAndIncrease(Long userId, Long questionId, KeyPrefix generalKeyPrefix, KeyPrefix countKeyPrefix) {
        likesCacheRepository.add(generalKeyPrefix, userId, questionId);
        likesCacheRepository.increase(countKeyPrefix, questionId);
    }
}
