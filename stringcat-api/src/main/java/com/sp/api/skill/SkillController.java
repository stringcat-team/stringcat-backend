package com.sp.api.skill;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/user/skill")
@Api(value = "SkillController : 스킬 관련 API")
public class SkillController {
}
