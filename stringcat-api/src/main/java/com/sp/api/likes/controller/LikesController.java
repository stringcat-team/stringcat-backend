package com.sp.api.likes.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.likes.dto.LikesPushRes;
import com.sp.api.likes.service.LikesService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @ApiOperation(value = "질문 좋아요 기능 (완료)")
    @PostMapping("/question/{questionId}/like")
    public ApiResponse<LikesPushRes> questionLikePush(@PathVariable("questionId") Long questionId) {
        return ApiResponse.success(likesService.questionLikePush(3L, questionId));
    }

    @ApiOperation(value = "질문 싫어요 기능 (완료)")
    @PostMapping("/question/{questionId}/dis-like")
    public ApiResponse<LikesPushRes> questionDisLikePush(
        @PathVariable("questionId") Long questionId) {
        return ApiResponse.success(likesService.questionDisLikePush(3L, questionId));
    }

    @ApiOperation(value = "답변 좋아요 기능 (완료)")
    @PostMapping("/answer/{answerId}/like")
    public ApiResponse<LikesPushRes> answerLikePush(@PathVariable("answerId") Long answerId) {
        return ApiResponse.success(likesService.answerLikePush(3L, answerId));
    }

    @ApiOperation(value = "답변 싫어요 기능 (완료)")
    @PostMapping("/answer/{answerId}/dis-like")
    public ApiResponse<LikesPushRes> answerDisLikePush(@PathVariable("answerId") Long answerId) {
        return ApiResponse.success(likesService.answerDisLikePush(3L, answerId));
    }
}