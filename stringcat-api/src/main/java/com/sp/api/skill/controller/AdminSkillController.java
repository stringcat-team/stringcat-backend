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

    @ApiOperation(value = "기술 사전 등록 API (완료)", notes = "해시태그로 사용될 기술 미리 등록하는 API, 추가되어야 할 기술명이 있다면 기술명 조회 API 확인 후 추가해주세요.")
    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody SkillReqDto.Register request) {
        skillService.register(request);

        return ApiResponse.success("OK");
    }

    @ApiOperation(value = "기술명 전체 조회 API (완료)", notes = "요청값 없이 DB에 있는 전체 기술값 조회하는 API")
    @GetMapping("/fetch")
    public ApiResponse<List<SkillResDto.SkillInfo>> fetch() {
        List<SkillResDto.SkillInfo> skillList = skillService.fetch();

        return ApiResponse.success(skillList);
    }

    @ApiOperation(value = "기술명 단건 조회 API (완료)", notes = "기술명 입력 후 해당 기술이 있는지 조회하는 API")
    @GetMapping("/search")
    public ApiResponse<String> search(SkillReqDto.Search request) {
        String res = skillService.search(request);

        return ApiResponse.success(res);
    }
}
