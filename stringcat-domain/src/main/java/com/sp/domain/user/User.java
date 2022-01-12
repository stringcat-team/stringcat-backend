package com.sp.domain.user;

import com.sp.domain.code.SocialType;
import com.sp.domain.code.UserRole;
import com.sp.domain.grade.Grade;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Accessors(chain = true)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "social_id")
    @Enumerated(EnumType.STRING)
    private SocialType socialId;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
    private String github;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column
    private String intro;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "image_path", columnDefinition = "TEXT")
    private String imagePath;

    @Column(name = "email_flag")
    private boolean emailFlag;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column
    private boolean deleted;

}