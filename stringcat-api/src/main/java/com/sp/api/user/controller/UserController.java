package com.sp.api.user.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.user.dto.UserReqDto;
import com.sp.api.user.dto.UserResDto;
import com.sp.api.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원 수정
    @ApiOperation(value = "회원 정보 수정 API (미완료)")
    @PatchMapping("/update/{id}")
    public ApiResponse<UserResDto.UserInfo> update(@Valid @RequestBody UserReqDto.UserInfo request) {
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

    //회원 조회
/*    @ApiOperation(value = "회원 조회 API", notes = "회원 검색 및 전체 조회 API 검색 키워드는 기술명과 닉네임으로 조회가능")
    @GetMapping("/search")
    public ApiResponse<Page<UserResDto.UserInfo>> search(@ModelAttribute UserReqDto.Search request,
                                                         @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                         @RequestParam(value = "size", defaultValue = "20", required = false) Integer size,
                                                         @RequestParam(value = "sort", defaultValue = "id", required = false) String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<UserResDto.UserInfo> response = userService.search(request, pageRequest);

        return ApiResponse.success(response);

    }*/

    //특정 회원 조회

}
