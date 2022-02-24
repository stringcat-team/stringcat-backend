package com.sp.api.follow;

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

    private long id;
    private long fromUserId;
    private long toUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
