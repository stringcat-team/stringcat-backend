package com.sp.api.question.dto;

import com.sp.domain.question.Question;
import io.swagger.models.auth.In;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResDto {
    private long questionId;
    private long userId;
    private String userNickname;
    private String title;
    private String contents;
    private int hits;
    private int likeCount;
    private int hateCount;
    private boolean likePushed;
    private boolean hatePushed;
    private int commentCount;
    private LocalDateTime createAt;
    private List<String> skills;

    public static QuestionResDto of(
        Question question,
        int likeCount,
        int disLikeCount,
        boolean likePushed,
        boolean disLikePushed) {
        return new QuestionResDto()
            .setQuestionId(question.getId())
            .setUserId(question.getUser().getId())
            .setUserNickname(question.getUser().getNickname())
            .setTitle(question.getTitle())
            .setContents(question.getContents())
            .setHits(question.getHits())
            .setLikeCount(likeCount)
            .setHateCount(disLikeCount)
            .setLikePushed(likePushed)
            .setHatePushed(disLikePushed)
            .setCommentCount(0)
            .setCreateAt(question.getCreatedAt())
            .setSkills(question.getQuestionSkills().stream()
                .map(questionSkill -> questionSkill.getSkill().getName())
                .collect(Collectors.toUnmodifiableList()));
    }
}
