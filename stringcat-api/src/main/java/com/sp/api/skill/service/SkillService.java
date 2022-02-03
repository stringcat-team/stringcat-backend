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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }

    public Skill getByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new StringcatCustomException("존재하지 않는 기술명입니다. 해당 기술을 등록해주세요.", ErrorCode.NOT_FOUND));
    }

    //skill register
    public void register(SkillReqDto.Register request) {
        Optional<Skill> nullCheck = findByName(request.getName());

        if(nullCheck.isPresent()) {
            throw new StringcatCustomException("이미 존재하는 값입니다.", ErrorCode.CONFLICT_EXCEPTION);
        }

        Skill skill = Skill.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();

        skill = skillRepository.save(skill);
    }

    //skill search
    public List<SkillResDto.SkillInfo> search(SkillReqDto.Search request) {
        List<Skill> skillList = skillRepository.findAll();


        return skillList
                .stream()
                .map(SkillResDto.SkillInfo::toDto)
                .collect(Collectors.toList());
    }

    public Skill toEntity(SkillResDto.SkillInfo skill) {
        return Skill.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();
    }
}
