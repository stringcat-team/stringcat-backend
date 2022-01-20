package com.sp.api.skill.service;

import com.sp.api.common.exception.ApiException;
import com.sp.api.skill.dto.SkillReqDto;
import com.sp.domain.skill.Skill;
import com.sp.domain.skill.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }

    //skill register
    public void register(SkillReqDto.Register request) {
        Optional<Skill> nullCheck = findByName(request.getName());

        if(!nullCheck.isEmpty()) {
            throw new ApiException("이미 존재하는 값입니다.");
        }

        Skill skill = Skill.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();

        skill = skillRepository.save(skill);
    }

    //skill search

    //skill
}
