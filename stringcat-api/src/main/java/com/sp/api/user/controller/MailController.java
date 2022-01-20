package com.sp.api.user.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.user.dto.MailReqDto;
import com.sp.api.user.service.MailService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    @ApiOperation(value = "메일 발송 API", notes = "사용자 이메일, 제목, 내용을 파라미터로 가짐")
    public ApiResponse<MailReqDto.MailTo> send(@Valid @RequestBody MailReqDto.MailTo request) {
        MailReqDto.MailTo mail = new MailReqDto.MailTo();

        mail.setEmail(request.getEmail());
        mail.setTitle(request.getTitle());
        mail.setContent(request.getContent());

        mailService.sendMail(mail);

        return ApiResponse.success(new MailReqDto.MailTo());
    }
}
