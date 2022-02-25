package com.sp.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    //is followed
    Long countByFromUserIdAndToUserId(Long id, Long userId);

    //following lists
    List<Follow> findByFromUserId(Long fromUserId);

    //followed lists
    List<Follow> findByToUserId(Long toUserId);

    //count following
    long countByFromUserId(Long fromUserId);

    //count followed
    long countByToUserId(Long toUserId);

    //unfollow
    @Modifying
    @Transactional
    void deleteByFromUserIdAndToUserId(Long from, Long to);
}
