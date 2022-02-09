package com.sp.api.user.controller;

import com.sp.api.advice.AdviceController;
import com.sp.api.common.dto.ApiResponse;
import com.sp.api.user.dto.UserReqDto;
import com.sp.api.user.dto.UserResDto;
import com.sp.api.user.service.UserService;
import com.sp.exception.type.ErrorCode;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController extends AdviceController {

    private final UserService userService;

    //회원 수정
    @ApiOperation(value = "회원 정보 수정 API (미완료)", notes = "해당 id값을 가진 사용자의 회원 정보 수정")
    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<UserResDto.UserInfo> update(@Valid @RequestBody UserReqDto.UpdateForm request) {
        Long userId = getUserId();

        if(Objects.isNull(userId)) {
            return ApiResponse.error(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        request.setId(getUserId());

        log.info("회원 수정 REQ :: userId : {}, request : {}", request.getId(), request.toString());

        return ApiResponse.success(new UserResDto.UserInfo());
    }

    //회원 탈퇴
    @ApiOperation(value = "회원 탈퇴 API (완료)", notes = "userId값 받아서 탈퇴")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        log.info("회원 탈퇴 REQ :: {}", id.toString());

        userService.delete(id);

        log.info("회원 탈퇴 RES :: {}", id.toString());

        return ApiResponse.success("SUCCESS");
    }

    //비밀번호 확인 API
    @ApiOperation(value = "비밀번호 확인 API (완료)", notes = "회원 수정 또는 회원 탈퇴 시 비밀번호 확인 후 넘어감")
    @PostMapping("/{id}/verify/password")
    public ApiResponse<Boolean> checkPassword(@PathVariable("id") Long id,
                                              @RequestBody UserReqDto.PasswordCheck request) {

        log.info("비밀번호 확인 REQ :: {}", id.toString());

        boolean response = userService.isMatchedPassword(id, request.getPassword());

        log.info("비밀번호 확인 RES :: {}", response);

        return ApiResponse.success(response);

    }

    //특정 회원 조회

}
