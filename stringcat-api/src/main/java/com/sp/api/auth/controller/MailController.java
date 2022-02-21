package com.sp.api.auth.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.user.dto.MailReqDto;
import com.sp.api.user.service.MailService;
import com.sp.domain.code.EmailType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send/verify")
    @ApiOperation(value = "회원가입시 메일 발송 API (미완료)", notes = "수신받을 이메일 파라미터")
    public ApiResponse<MailReqDto.MailTo> authMail(@Valid @RequestBody MailReqDto.MailTo request) {
        MailReqDto.MailTo mail = new MailReqDto.MailTo();

        mail.setEmail(request.getEmail());
        mail.setType(EmailType.VERIFIER);
        mail.setTitle(request.getTitle());
        mail.setContent(request.getContent());

        mailService.sendMail(mail);

        return ApiResponse.success(new MailReqDto.MailTo());
    }

    @PostMapping("/send/password")
    @ApiOperation(value = "비밀번호 찾기 API (미완료)", notes = "이메일로 요청을 받음")
    public ApiResponse<MailReqDto.MailTo> setPassword(@Valid @RequestBody MailReqDto.MailTo request) {
        MailReqDto.MailTo mail = new MailReqDto.MailTo();

        mail.setEmail(request.getEmail());
        mail.setType(EmailType.PASSWORD_SENDER);
        mail.setTitle(request.getTitle());
        mail.setContent(request.getContent());

        mailService.sendMail(mail);

        return ApiResponse.success(new MailReqDto.MailTo());
    }

}
