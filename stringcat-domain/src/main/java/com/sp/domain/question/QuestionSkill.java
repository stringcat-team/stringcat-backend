package com.sp.domain.question;

import com.sp.domain.skill.Skill;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "question_skill")
public class QuestionSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public QuestionSkill(Question question, Long skillId) {
        this.question = question;
        this.skill = Skill.builder().id(skillId).build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill.getId(), question.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        QuestionSkill questionSkill = (QuestionSkill) obj;
        return Objects.equals(skill.getId(), questionSkill.getSkill().getId())
            && Objects.equals(question.getId(), questionSkill.getQuestion().getId());
    }
}
