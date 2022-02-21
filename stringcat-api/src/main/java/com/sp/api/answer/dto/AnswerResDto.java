package com.sp.api.answer.dto;

import com.sp.domain.answer.Answer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResDto {
    private Long id;
    private Long questionId;
    private long userId;
    private String userNickname;
    private String contents;
    private boolean selected;
    private int likeCount;
    private int hateCount;
    private boolean likePushed;
    private boolean hatePushed;
    private int commentCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static AnswerResDto of(Answer answer,
        int likeCount,
        int disLikeCount,
        boolean likePushed,
        boolean disLikePushed) {
        return new AnswerResDto()
            .setId(answer.getId())
            .setQuestionId(answer.getQuestion().getId())
            .setUserId(answer.getUser().getId())
            .setUserNickname(answer.getUser().getNickname())
            .setContents(answer.getContents())
            .setSelected(answer.isSeleted())
            .setLikeCount(likeCount)
            .setHateCount(disLikeCount)
            .setLikePushed(likePushed)
            .setHatePushed(disLikePushed)
            .setCreateAt(answer.getCreatedAt())
            .setUpdateAt(answer.getUpdatedAt());
    }
}
