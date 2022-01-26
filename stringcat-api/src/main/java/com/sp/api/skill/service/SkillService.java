package com.sp.api.skill.service;

import com.sp.api.skill.dto.SkillReqDto;
import com.sp.api.skill.dto.SkillResDto;
import com.sp.domain.skill.Skill;
import com.sp.domain.skill.SkillRepository;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
            throw new StringcatCustomException("이미 존재하는 값입니다.", ErrorCode.CONFLICT_EXCEPTION);
        }

        Skill skill = Skill.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();

        skill = skillRepository.save(skill);
    }

    //skill search
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public SkillResDto.SkillInfo toEntity(Skill skill) {
        return new SkillResDto.SkillInfo()
                .setId(skill.getId())
                .setName(skill.getName());
    }
}
