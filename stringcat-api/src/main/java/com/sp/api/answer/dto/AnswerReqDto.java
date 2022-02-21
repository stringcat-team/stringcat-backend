package com.sp.api.answer.dto;

import com.sp.domain.answer.Answer;
import com.sp.domain.question.Question;
import com.sp.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public class AnswerReqDto {

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String contents;

        public Answer toEntity(Question question, User user) {
            return Answer.builder()
                .question(question)
                .user(user)
                .contents(contents)
                .build();
        }
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String contents;
    }
}
