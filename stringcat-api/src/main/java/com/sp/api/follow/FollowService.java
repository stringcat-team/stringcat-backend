package com.sp.api.follow;

import com.sp.api.user.service.UserService;
import com.sp.domain.follow.Follow;
import com.sp.domain.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    public void follow(FollowReqDto.RequestFollow request) {
        Follow follow = Follow.builder()
                .fromUserId(request.getUserId())
                .toUserId(request.getToUserId())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
