package com.sp.api.follow;

import com.sp.api.common.dto.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/follow")
@RequiredArgsConstructor
@Api(value = "FollowController - Follow 관련 API")
public class FollowController {

    private final FollowService followService;

    @ApiOperation(value = "팔로우하기 API")
    @PostMapping
    public ApiResponse<FollowResDto> follow(@RequestBody FollowReqDto.RequestFollow request) {
        log.info("팔로잉 REQ :: {}", request.toString());

        FollowResDto res = followService.follow(request);

        log.info("팔로잉 RES :: {}", res.toString());

        return ApiResponse.success(res);
    }
}
