package com.sp.api.follow;

import com.sp.api.user.service.UserService;
import com.sp.domain.follow.Follow;
import com.sp.domain.follow.FollowRepository;
import com.sp.domain.user.User;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    public FollowResDto follow(FollowReqDto.FollowUnfollow request) {
        Optional<User> proxyUser = userService.findById(request.getToUserId());

        if (proxyUser.isPresent()) { User toUser = proxyUser.get(); }

        Follow follow = Follow.builder()
                .fromUserId(userService.getUserId())
                .toUserId(request.getToUserId())
                .createdAt(LocalDateTime.now())
                .build();

        followRepository.save(follow);

        return FollowResDto.of(follow);
    }

    public void unfollow(FollowReqDto.FollowUnfollow request) {
    }

}
