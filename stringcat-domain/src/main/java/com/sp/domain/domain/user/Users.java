package com.sp.domain.domain.user;

import com.sp.domain.code.UserRole;
import com.sp.domain.domain.grade.Grade;
import com.sp.domain.domain.userskill.UserSkill;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne
    @JoinColumn(name = "grade_id")
    public Grade grade;

    @Column
    @Enumerated(EnumType.STRING)
    public UserRole role;

    @Column(name = "social_id")
    public String socialId;

    @Column
    public String email;

    @Column
    public String password;

    @Column
    public String nickname;

    @Column
    public String github;

    @Column
    public String bio;

    @Column(columnDefinition = "TEXT")
    public String intro;

    @Column(name = "image_path")
    public String imagePath;

    @Column(name = "email_flag")
    public boolean emailFlag;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column
    public boolean deleted;

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setEmailFlag(boolean emailFlag) {
        this.emailFlag = emailFlag;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
