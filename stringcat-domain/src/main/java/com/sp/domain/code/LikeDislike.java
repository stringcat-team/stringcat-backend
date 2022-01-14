package com.sp.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeDislike {

    LIKE("like", "좋아요", true),
    DISLIKE("dislike", "싫어요", true);

    private final String name;
    private final String desc;
    private final boolean active;

}
