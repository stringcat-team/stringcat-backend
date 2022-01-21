package com.sp.domain.grade;

import com.sp.domain.code.GradeRange;
import lombok.*;
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
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private GradeRange name;

    @Column(name = "min_score")
    private int minScore;

    @Column(name = "max_score")
    private int maxScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}
