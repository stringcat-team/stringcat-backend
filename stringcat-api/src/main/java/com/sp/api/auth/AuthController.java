package com.sp.api.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
@Api(value = "로그인 및 회원가입 관련 Api")
public class AuthController {

    @ApiOperation(value = "헬스체크")
    @GetMapping("/hello")
    public String hello() {
        return "OK";
    }

}
