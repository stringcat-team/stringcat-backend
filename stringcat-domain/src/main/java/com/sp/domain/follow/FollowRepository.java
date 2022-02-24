package com.sp.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Long countByFromUserIdAndToUserId(Long id, Long userId);

    @Modifying
    @Transactional
    void deleteByFromUserIdAndToUserId(Long from, Long to);
}
