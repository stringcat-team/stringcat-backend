package com.sp.api.user.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.user.dto.UserReqDto;
import com.sp.api.user.dto.UserResDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    //회원 수정
    @ApiOperation(value = "회원 정보 수정 API")
    @PatchMapping("/update/{id}")
    public ApiResponse<UserResDto.UserInfo> update(@Valid @RequestBody UserReqDto.UserInfo request) {
        return ApiResponse.success(new UserResDto.UserInfo());
    }

    //회원 탈퇴
    @ApiOperation(value = "회원 탈퇴 API", notes = "userId값 받아서 탈퇴")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@Valid @PathVariable Long id) {
        return ApiResponse.success("OK");
    }

    //비밀번호 확인 API
    @ApiOperation(value = "비밀번호 확인 API", notes = "회원 수정 또는 회원 탈퇴 시 비밀번호 확인 후 넘어감")
    @PostMapping("/check")
    public ApiResponse<String> checkPassword(@Valid @RequestBody UserReqDto.PasswordCheck request) {
        return ApiResponse.success("OK");
    }

}
