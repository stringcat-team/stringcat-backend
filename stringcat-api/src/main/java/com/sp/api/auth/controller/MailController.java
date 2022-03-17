package com.sp.api.auth.controller;

import com.sp.api.auth.dto.MailReqDto;
import com.sp.api.auth.service.EmailService;
import com.sp.api.common.dto.ApiResponse;
import com.sp.domain.code.EmailType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth/mail")
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    @PostMapping("/verify/email")
    @ApiOperation(value = "이메일 인증코드 확인 API (완료)", notes = "이메일로 받은 인증코드가 일치하는지 확인, 보안을 위해 GET이 아닌 POST 사용")
    public ApiResponse<String> verifyEmailCode(@RequestBody MailReqDto.MailTo request) throws Exception {
        log.info("이메일 인증코드 확인 REQ :: {}", request.toString());

        String result = emailService.checkEmailCode(request);

        log.info("이메일 인증코드 확인 RES :: {}", result);

        return ApiResponse.success(result);
    }


    @PostMapping("/send/verify")
    @ApiOperation(value = "회원가입시 메일 발송 API (완료)", notes = "수신받을 이메일 파라미터")
    public ApiResponse<String> authMail(@RequestBody @ApiParam(
            value="수신인 이메일 정보", required = true) String email, EmailType type) throws Exception {
        log.info("이메일 발송 REQ :: {} {}", email, type.getName());

        String confirm = emailService.sendEmail(email, type);

        log.info("이메일 발송 RES :: {}", confirm);

        return ApiResponse.success(confirm);
    }

    @PostMapping("/send/password")
    @ApiOperation(value = "비밀번호 찾기 API (완료)", notes = "이메일로 요청을 받음")
    public ApiResponse<MailReqDto.MailTo> setPassword(@RequestBody String email, EmailType type) throws Exception {
        String tmpPwd = emailService.sendEmail(email, type);

        return ApiResponse.success(new MailReqDto.MailTo());
    }
}
