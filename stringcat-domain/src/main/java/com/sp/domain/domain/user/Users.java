package com.sp.domain.domain.user;

import com.sp.domain.code.AuthProviders;
import com.sp.domain.code.UserRole;
import com.sp.domain.domain.grade.Grade;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    public AuthProviders authProvider;

    @Column(name = "social_id")
    public AuthProviders socialId;

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

    public String refreshToken;

    public Users refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;

        return this;
    }

}
