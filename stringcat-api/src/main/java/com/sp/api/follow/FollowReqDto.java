package com.sp.api.follow;

import com.sp.domain.follow.Follow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public class FollowReqDto {

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowUnfollow {
        private Long toUserId;
    }


}
