package com.sp.api.skill.controller;

import com.sp.api.skill.dto.SkillReqDto;
import com.sp.api.skill.service.SkillService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/skill")
@RequiredArgsConstructor
public class AdminSkillController {

    private final SkillService skillService;

    @ApiOperation(value = "기술 사전 등록 API", notes = "해시태그로 사용될 기술 미리 등록하는 API")
    @PostMapping("/register")
    public String register(@Valid @RequestBody SkillReqDto.Register request) {
        skillService.register(request);

        return "OK";
    }
}
