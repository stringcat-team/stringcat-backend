package com.sp.api.likes.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikesPushRes {
    private boolean isPushed;

    private LikesPushRes(boolean isPushed) {
        this.isPushed = isPushed;
    }

    public static LikesPushRes of(boolean isPushed) {
        return new LikesPushRes(isPushed);
    }
}
