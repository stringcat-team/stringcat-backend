package com.sp.api.follow;

import com.sp.api.user.service.UserService;
import com.sp.domain.follow.Follow;
import com.sp.domain.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    public FollowResDto follow(FollowReqDto.RequestFollow request) {
        Follow follow = Follow.builder()
                .fromUserId(request.getFromUserId())
                .toUserId(request.getToUserId())
                .createdAt(LocalDateTime.now())
                .build();

        followRepository.save(follow);

        return FollowResDto.of(follow);
    }

}
