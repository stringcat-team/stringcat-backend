package com.sp.api.follow;

import com.sp.domain.follow.Follow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FollowResDto {

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private int followingCnt;
    private int followedCnt;
    private LocalDateTime createdAt;

    public static FollowResDto of(Follow follow) {
        return new FollowResDto()
                .setId(follow.getId())
                .setFromUserId(follow.getFromUserId())
                .setToUserId(follow.getToUserId())
                .setCreatedAt(follow.getCreatedAt());
    }

}
