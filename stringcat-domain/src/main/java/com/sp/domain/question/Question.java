package com.sp.domain.question;

import com.sp.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column
    private int hits;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column
    private boolean deleted;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<QuestionSkill> questionSkills = new ArrayList<>();

    public Question(User user, String title, String contents) {
        this.user = user;
        this.title = title;
        this.contents = contents;
    }

    public void addQuestionSKills(List<QuestionSkill> questionSkills) {
        this.questionSkills.addAll(questionSkills);
    }

    public void update(String title, String contents, List<QuestionSkill> questionSkills) {
        this.title = title;
        this.contents = contents;
        this.updatedAt = LocalDateTime.now();

        this.questionSkills.removeIf(questionSkill -> !questionSkills.contains(questionSkill));
        this.questionSkills.addAll(questionSkills.stream()
            .filter(questionSkill -> !this.questionSkills.contains(questionSkill))
            .collect(Collectors.toList()));
    }

    public void delete() {
        this.deleted = true;
        this.updatedAt = LocalDateTime.now();
    }
}
