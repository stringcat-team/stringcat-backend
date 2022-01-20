package com.sp.api.question.dto;

import com.sp.domain.question.Question;
import com.sp.domain.question.QuestionSkill;
import com.sp.domain.skill.Skill;
import com.sp.domain.user.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public class QuestionReqDto {

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {
        private String keyword;
        private String condition;

        private int cursor;
        private int size;
        private String sort;

        private List<Long> skills;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String title;
        private String contents;
        private List<Long> skills;

        public Question toEntity(User user) {
            Question question = new Question(user, title, contents);

            question.addQuestionSKills(skills.stream()
                .map(id -> new QuestionSkill(question, id))
                .collect(Collectors.toList()));

            return question;
        }
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String title;
        private String contents;
        private List<Long> skills;
    }
}