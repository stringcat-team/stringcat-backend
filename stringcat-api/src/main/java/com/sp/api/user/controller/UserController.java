package com.sp.api.user.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.user.dto.UserReqDto;
import com.sp.api.user.dto.UserResDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    //회원 수정
    @ApiOperation(value = "회원 정보 수정 API")
    @PatchMapping("/update/{id}")
    public ApiResponse update(@Valid @RequestBody UserReqDto.UserInfo request) {
        return ApiResponse.success(new UserResDto.UserInfo());
    }

    //회원 탈퇴


}
