package com.sp.domain.domain.userskill;

import com.sp.domain.domain.skill.Skill;
import com.sp.domain.domain.user.Users;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "user_skill")
public class UserSkill {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id")
        private Users user;

        @ManyToOne
        @JoinColumn(name = "skill_id")
        private Skill skill;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

}
