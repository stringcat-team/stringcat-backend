package com.sp.api.grade.service;

import com.sp.api.common.exception.ApiException;
import com.sp.api.grade.dto.GradeResDto;
import com.sp.domain.grade.Grade;
import com.sp.domain.grade.GradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    public Optional<Grade> findById(Long id) {
        return gradeRepository.findById(id);
    }

    public Grade getById(Long id) {
        return findById(id)
                .orElseThrow(() -> {
                    throw new ApiException("없는 등급입니다.");
                });
    }

    public Grade toEntity(GradeResDto.GradeInfo grade) {
        return Grade.builder()
                .id(grade.getId())
                .name(grade.getName())
                .minScore(grade.getMinScore())
                .maxScore(grade.getMaxScore())
                .createdAt(grade.getCreatedAt())
                .updatedAt(grade.getUpdatedAt())
                .build();
    }
}