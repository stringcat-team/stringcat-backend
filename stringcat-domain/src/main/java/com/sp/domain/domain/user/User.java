package com.sp.domain.domain.user;

import com.sp.domain.domain.grade.Grade;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column(name = "social_id")
    private String socialId;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
    private String github;

    @Column
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String intro;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "email_flag")
    private boolean emailFlag;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<UserSkill> userSkills = new ArrayList<>();

    @Column
    private boolean deleted;

    public User() {

    }
}
