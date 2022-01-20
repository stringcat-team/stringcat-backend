package com.sp.domain.question;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QuestionStatus {
    ACTIVE(false),
    DELETED(true);

    private final boolean status;
}
