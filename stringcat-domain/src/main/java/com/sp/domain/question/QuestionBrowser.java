package com.sp.domain.question;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class QuestionBrowser {
    private String keyword;
    private String condition;

    private int cursor;
    private int size;

    private String sort;
    private List<Long> skills;
}
