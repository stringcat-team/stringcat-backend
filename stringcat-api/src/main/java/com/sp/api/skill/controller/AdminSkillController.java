package com.sp.api.skill.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.skill.dto.SkillReqDto;
import com.sp.api.skill.dto.SkillResDto;
import com.sp.api.skill.service.SkillService;
import com.sp.domain.skill.Skill;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/skill")
@RequiredArgsConstructor
public class AdminSkillController {

    private final SkillService skillService;

    @ApiOperation(value = "기술 사전 등록 API", notes = "해시태그로 사용될 기술 미리 등록하는 API, 추가되어야 할 기술명이 있다면 기술명 조회 API 확인 후 추가해주세요.")
    @PostMapping("/register")
    public String register(@Valid @RequestBody SkillReqDto.Register request) {
        skillService.register(request);

        return "OK";
    }

    @ApiOperation(value = "기술명 조회 API", notes = "기술명 중복시 등록이 불가합니다. 여기서 확인 후 추가해주세요.")
    @GetMapping("/fetch")
    public ApiResponse<List<SkillResDto.SkillInfo>> search(SkillReqDto.Search request) {
        List<SkillResDto.SkillInfo> skillList = skillService.search(request);

        return ApiResponse.success(skillList);
    }
}
